package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.category.CategoryCreateRequest;
import com.example.ecommerce.dto.request.category.CategoryUpdateRequest;
import com.example.ecommerce.dto.response.category.CategoryResponse;
import com.example.ecommerce.dto.response.common.ApiResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.exception.category.CategoryErrorCode;
import com.example.ecommerce.exception.category.CategoryException;
import com.example.ecommerce.mapper.CategoryMapper;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.util.SlugUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryCreateRequest request){

        Category category = categoryMapper.toCategory(request);
        category.setSlug(SlugUtils.generateSlug(request.getName()));

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    public CategoryResponse updateCategory(Long categoryId,  CategoryUpdateRequest request){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        // update slug when category name change
        String newSlug = SlugUtils.generateSlug(request.getName());
        if (!category.getSlug().equals(newSlug)) {

            // check slug uniqueness
            if (categoryRepository.findBySlug(newSlug).isPresent())
                newSlug = newSlug  + '-' + System.currentTimeMillis();

            category.setSlug(newSlug);
        }

        categoryMapper.updateCategory(category, request);

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    public CategoryResponse getCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        return categoryMapper.toCategoryResponse(category);
    }

    public void deleteCategory(Long categoryId){
        categoryRepository.deleteById(categoryId);
    }

    public List<CategoryResponse> getCategories(){
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).toList();
    }
}
