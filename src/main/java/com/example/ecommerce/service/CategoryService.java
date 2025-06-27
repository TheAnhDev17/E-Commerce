package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.CategoryCreateRequest;
import com.example.ecommerce.dto.response.CategoryResponse;
import com.example.ecommerce.mapper.CategoryMapper;
import com.example.ecommerce.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryCreateRequest request){
        return categoryMapper.toCategoryResponse(categoryRepository.save(categoryMapper.toCategory(request)));
    }
}
