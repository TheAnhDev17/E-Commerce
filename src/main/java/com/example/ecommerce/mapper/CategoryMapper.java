package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.CategoryCreateRequest;
import com.example.ecommerce.dto.response.CategoryResponse;
import com.example.ecommerce.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreateRequest request);

    CategoryResponse toCategoryResponse(Category category);
}
