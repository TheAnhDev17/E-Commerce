package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.product.ProductCreateRequest;
import com.example.ecommerce.dto.request.product.ProductUpdateRequest;
import com.example.ecommerce.dto.request.product.ProductVariantCreateRequest;
import com.example.ecommerce.dto.response.common.ApiResponse;
import com.example.ecommerce.dto.response.product.ProductResponse;
import com.example.ecommerce.dto.response.product.ProductVariantResponse;
import com.example.ecommerce.repository.ProductVariantRepository;
import com.example.ecommerce.service.product.ProductService;
import com.example.ecommerce.service.product.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/products")
@Slf4j
public class ProductController {
    ProductService productService;
    ProductVariantService productVariantService;

    @PostMapping
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductCreateRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @PostMapping("/{productId}/images")
    public ApiResponse<ProductResponse> uploadProductImages(
            @PathVariable Long productId,
            @RequestParam("files") MultipartFile[] files) {

        log.info("Uploading {} images to product ID: {}", files.length, productId);

        ProductResponse product = productService.addProductImages(productId, files);

        return ApiResponse.<ProductResponse>builder()
                .message("Images uploaded successfully")
                .result(product)
                .build();
    }

    @PutMapping("/{productId}/images")
    public ApiResponse<ProductResponse> updateProductImages(
            @PathVariable Long productId,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam(value = "keepImageIds", required = false) List<Long> keepImageIds
    ) {

        ProductResponse product = productService.updateProductImage(productId, files, keepImageIds);

        return ApiResponse.<ProductResponse>builder()
                .message("Images update successfully")
                .result(product)
                .build();
    }

    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest request){
        ProductResponse product = productService.updateProduct(productId, request);

        return ApiResponse.<ProductResponse>builder()
                .result(product)
                .build();
    }

    @GetMapping()
    public ApiResponse<List<ProductResponse>> getProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getProducts())
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long productId) {
        ProductResponse response = productService.getProductById(productId);

        return ApiResponse.<ProductResponse>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);

        return ApiResponse.<Void>builder()
                .message("Product deleted successfully")
                .build();
    }

    // Product variant
    @PutMapping("/{productId}/variants")
    public ApiResponse<ProductVariantResponse> createProductVariant(
            @PathVariable Long productId,
            @RequestBody ProductVariantCreateRequest request) {

        return ApiResponse.<ProductVariantResponse>builder()
                .result(productVariantService.createProductVariant(productId, request))
                .build();
    }

    @GetMapping("/{productId}/variants")
    public ApiResponse<List<ProductVariantResponse>> getProductVariants(@PathVariable Long productId) {
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .result(productVariantService.getProductVariants(productId))
                .build();
    }
}
