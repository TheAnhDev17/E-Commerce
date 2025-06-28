package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.ProductImageCreateRequest;
import com.example.ecommerce.dto.response.ProductImageResponse;
import com.example.ecommerce.entity.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImage toProductImage (ProductImageCreateRequest request);

    ProductImageResponse toProductImageResponse(ProductImage productImage);
}
