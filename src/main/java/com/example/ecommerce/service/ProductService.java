package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.ProductCreateRequest;
import com.example.ecommerce.dto.request.ProductUpdateRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductImage;
import com.example.ecommerce.exception.file.FileErrorCode;
import com.example.ecommerce.exception.file.FileException;
import com.example.ecommerce.exception.product.ProductErrorCode;
import com.example.ecommerce.exception.product.ProductException;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.util.SlugUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    FileUploadService fileUploadService;

    public ProductResponse createProduct(ProductCreateRequest request){
        Product product = productMapper.toProduct(request);

        product.setSlug(SlugUtils.generateSlug(request.getName()));

        if(request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()){
            Set<Category> categories = new HashSet<>();

            for (Long categoryId : request.getCategoryIds()){
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

                categories.add(category);
            }

            product.setCategories(categories);
        }

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request){

        // 1. Find existing product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 2. Validate SKU uniqueness (when sku change)
        if (!product.getSku().equals(request.getSku()) && productRepository.findBySku(request.getSku()).isPresent())
                throw new ProductException(ProductErrorCode.SKU_EXISTED);


        // 3. Update basic fields
        productMapper.updateProduct(product, request);

        // 4. Update product categories

        updateProductCategories(product, request.getCategoryIds());

        // Update slug when slug change
        String newSlug = SlugUtils.generateSlug(request.getName());
        if (!product.getSlug().equals(newSlug)) {
            // Check slug uniqueness
            if (productRepository.findBySlug(newSlug).isPresent()) {
                newSlug = newSlug + "-" + System.currentTimeMillis();
            }
            product.setSlug(newSlug);
        }


        product = productRepository.save(product);

        return productMapper.toProductResponse(product);
    }

    private void updateProductCategories(Product product, List<Long> categoryIds){
        log.info("Updating categories for product ID: {} with category IDs: {}", product.getId(), categoryIds);

        // 1. Clear existing categories
        product.getCategories().clear();

        // 2. Validate and load new categories
        if (categoryIds != null && !categoryIds.isEmpty()){
            Set<Category> categories = new HashSet<>();

            for (Long categoryId : categoryIds){
                Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));

                // check category is active
                if(Boolean.FALSE.equals(category.getIsActive()))
                    throw new ProductException(ProductErrorCode.PRODUCT_CATEGORY_INACTIVE);

                categories.add(category);
            }

            // Verify tất cả categories đều được tìm thấy
            if (categories.size() != categoryIds.size()) {
                throw new ProductException(ProductErrorCode.PRODUCT_INVALID_CATEGORIES);
            }

            // 3. Set new categories
            product.setCategories(categories);

            log.info("Updated product categories: {}",
                    categories.stream().map(Category::getName).toList());
        }
    }


    public ProductResponse addProductImages(Long productId, MultipartFile[] files){
        log.info("Adding {} images to product ID: {}", files.length, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        addNewImages(product, files);

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    public ProductResponse updateProductImage(Long productId, MultipartFile[] newFiles, List<Long> keepImageIds){
        log.info("Update images for product {}: keeping {} old images, adding {} new images",
                productId,
                keepImageIds != null ? keepImageIds.size() : 0,
                newFiles != null ? newFiles.length : 0);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

        // 1. Xử lý ảnh cũ: giữ lại hoặc xóa
        handleExistingImages(product, keepImageIds);

        // 2. Thêm ảnh mới
        if (newFiles != null && newFiles.length > 0) {
            addNewImages(product, newFiles);
        }

        // 3. Tự động fix primary image và sort order
        autoFixImagesOrder(product);

        return productMapper.toProductResponse(productRepository.save(product));
    }

    private void handleExistingImages(Product product, List<Long> keepImageIds){
        if(keepImageIds == null){
            keepImageIds = Collections.emptyList();
        }

        // Tìm images cần xóa(những images không có trong keepImageIds)
        List<Long> finalKeepImageIds = keepImageIds;
        List<ProductImage> imagesToDelete = product.getImages().stream()
                .filter(img -> !finalKeepImageIds.contains(img.getId()))
                .toList();

        for(ProductImage imageToDelete : imagesToDelete){
            log.info("Deleting image: {}", imageToDelete.getImageUrl());

            // Xóa file từ storage
            try {
                fileUploadService.deleteFile(imageToDelete.getImageUrl());
            } catch (Exception e) {
                log.warn("Failed to delete image file: {}", imageToDelete.getImageUrl());
            }

            // Remove từ product
            product.getImages().remove(imageToDelete);
        }

        log.info("Deleted {} old images, kept {} images",
                imagesToDelete.size(),
                product.getImages().size());
    }

    private void addNewImages(Product product, MultipartFile[] newFiles){
        validateImageFiles(newFiles);

        int nextSortOrder = getNextSortOrder(product.getImages());
        boolean shouldSetPrimary = product.getImages().isEmpty();

        List<ProductImage> newImages = new ArrayList<>();

        for (int i = 0; i < newFiles.length; i++) {
            MultipartFile file = newFiles[i];

            try {
                String imageUrl = fileUploadService.uploadFile(file, "products");

                ProductImage newImage = createProductImage(
                        product,
                        imageUrl,
                        nextSortOrder + i,
                        shouldSetPrimary && i == 0  // Chỉ image đầu tiên làm primary
                );

                newImages.add(newImage);
                log.info("Added new image: {}", imageUrl);

            } catch (Exception e) {
                log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                throw new FileException(FileErrorCode.FILE_UPLOAD_FAILED, e);
            }
        }

        // Bulk add để tối ưu performance
        product.getImages().addAll(newImages);
    }

    private ProductImage createProductImage(Product product, String imageUrl, int sortOrder, boolean isPrimary) {
        return ProductImage.builder()
                .product(product)
                .imageUrl(imageUrl)
                .altText(product.getName() + " - Image " + (sortOrder + 1))
                .sortOrder(sortOrder)
                .isPrimary(isPrimary)
                .build();
    }

    private void validateImageFiles(MultipartFile[] files) {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new FileException(FileErrorCode.FILE_EMPTY);
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new FileException(FileErrorCode.INVALID_FILE_TYPE);
            }

            if (file.getSize() > 5 * 1024 * 1024) { // 5MB
                throw new FileException(FileErrorCode.FILE_SIZE_EXCEEDED);
            }
        }
    }

    private int getNextSortOrder(Set<ProductImage> images) {
        if (images == null || images.isEmpty()) {
            return 0; // Bắt đầu từ 0 cho product mới
        }

        return images.stream()
                .mapToInt(ProductImage::getSortOrder)
                .max()
                .orElse(-1) + 1;
    }

    private void autoFixImagesOrder(Product product) {
        if (product.getImages().isEmpty()) {
            return;
        }

        // 1. Sắp xếp lại sort order
        List<ProductImage> sortedImages = product.getImages().stream()
                .sorted(Comparator.comparing(ProductImage::getSortOrder))
                .toList();

        for (int i = 0; i < sortedImages.size(); i++) {
            sortedImages.get(i).setSortOrder(i);
        }

        // 2. Đảm bảo có ít nhất 1 primary image
        boolean hasPrimary = product.getImages().stream()
                .anyMatch(ProductImage::getIsPrimary);

        if (!hasPrimary) {
            sortedImages.getFirst().setIsPrimary(true);
            log.info("Set first image as primary");
        }
    }



    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        return productMapper.toProductResponse(product);
    }
}
