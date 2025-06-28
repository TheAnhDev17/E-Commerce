package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.ProductCreateRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductImage;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

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
