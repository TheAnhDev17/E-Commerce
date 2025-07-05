package com.example.ecommerce.mapper.product;

import com.example.ecommerce.dto.request.product.ProductVariantCreateRequest;
import com.example.ecommerce.dto.response.product.ProductVariantResponse;
import com.example.ecommerce.entity.ProductVariant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariant toProductVariant(ProductVariantCreateRequest request);

    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);
}
