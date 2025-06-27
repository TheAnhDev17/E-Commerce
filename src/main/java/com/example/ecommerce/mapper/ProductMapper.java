package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.ProductCreateRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categories", ignore = true)
    Product toProduct(ProductCreateRequest request);

    ProductResponse toProductResponse(Product product);
}
