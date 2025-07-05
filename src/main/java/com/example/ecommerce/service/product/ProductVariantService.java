package com.example.ecommerce.service.product;

import com.example.ecommerce.dto.request.product.ProductVariantCreateRequest;
import com.example.ecommerce.dto.response.product.ProductVariantResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductVariant;
import com.example.ecommerce.exception.product.ProductErrorCode;
import com.example.ecommerce.exception.product.ProductException;
import com.example.ecommerce.mapper.product.ProductVariantMapper;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.ProductVariantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantService {

    ProductVariantRepository productVariantRepository;
    ProductRepository productRepository;
    ProductVariantMapper productVariantMapper;

    public ProductVariantResponse createProductVariant(Long productId, ProductVariantCreateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));
        // Validate SKU uniqueness
        if (productVariantRepository.existsBySku(request.getSku()))
            throw new ProductException(ProductErrorCode.VARIANT_SKU_EXISTED);

        ProductVariant productVariant = productVariantMapper.toProductVariant(request);
        productVariant.setProduct(product);

        productVariant = productVariantRepository.save(productVariant);

        return productVariantMapper.toProductVariantResponse(productVariant);
    }

    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getProductVariants(Long productId) {
        List<ProductVariant> variants = productVariantRepository
                .findByProductIdAndIsActiveTrueOrderByCreatedAt(productId);

        return variants.stream().map(productVariantMapper::toProductVariantResponse).toList();
    }
}
