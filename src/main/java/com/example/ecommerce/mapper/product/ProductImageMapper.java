package com.example.ecommerce.mapper.product;

import com.example.ecommerce.dto.request.product.ProductImageCreateRequest;
import com.example.ecommerce.dto.response.product.ProductImageResponse;
import com.example.ecommerce.entity.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImage toProductImage (ProductImageCreateRequest request);

    ProductImageResponse toProductImageResponse(ProductImage productImage);
}
