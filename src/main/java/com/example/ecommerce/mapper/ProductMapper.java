package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.product.ProductCreateRequest;
import com.example.ecommerce.dto.request.product.ProductUpdateRequest;
import com.example.ecommerce.dto.response.product.ProductResponse;
import com.example.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductCreateRequest request);

    ProductResponse toProductResponse(Product product);

    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);
}
