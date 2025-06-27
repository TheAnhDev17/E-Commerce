package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.ProductCreateRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;

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
}
