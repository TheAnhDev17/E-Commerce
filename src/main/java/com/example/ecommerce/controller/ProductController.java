package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.ProductCreateRequest;
import com.example.ecommerce.dto.response.ApiResponse;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
public class ProductController {
    ProductService productService;

    @PostMapping
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductCreateRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }
}
