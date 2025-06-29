package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.ProductCreateRequest;
import com.example.ecommerce.dto.request.ProductUpdateRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductImage;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.util.SlugUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. Validate SKU uniqueness (when sku change)
        if (!product.getSku().equals(request.getSku())){
            if (productRepository.findBySku(request.getSku()).isPresent())
                throw new RuntimeException("SKU already exists: " + request.getSku());
        }

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
//                if(Boolean.FALSE.equals(category.getIsActive()))
//                    throw new RuntimeException("Category is inactive: " + category.getName());

                categories.add(category);
            }

            // Verify tất cả categories đều được tìm thấy
            if (categories.size() != categoryIds.size()) {
                throw new RuntimeException("Some categories were not found or inactive");
            }

            // 3. Set new categories
            product.setCategories(categories);

            log.info("Updated product categories: {}",
                    categories.stream().map(Category::getName).collect(Collectors.toList()));
        }
    }


    public ProductResponse addProductImages(Long productId, MultipartFile[] files){
        log.info("Adding {} images to product ID: {}", files.length, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        int currentImageCount = product.getImages().size();

        for (int i = 0; i < files.length; i++){
            MultipartFile file = files[i];

            try {
                String imageUrl = fileUploadService.uploadFile(file, "product");

                ProductImage productImage = ProductImage.builder()
                        .product(product)
                        .imageUrl(imageUrl)
                        .altText(product.getName() + " - Image " + (currentImageCount + i + 1))
                        .sortOrder(currentImageCount + i)
                        .isPrimary(product.getImages().isEmpty() && i == 0) // First image if no existing images
                        .build();

                product.getImages().add(productImage);

            } catch (Exception e){
                log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
            }
        }
        Product savedProduct = productRepository.save(product);

        return productMapper.toProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        return productMapper.toProductResponse(product);
    }
}
