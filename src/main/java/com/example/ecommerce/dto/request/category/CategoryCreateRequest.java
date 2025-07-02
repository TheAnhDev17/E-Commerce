package com.example.ecommerce.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreateRequest {

    @NotBlank(message = "CATEGORY_NAME_REQUIRED")
    String name;

    String slug;
    String description;
    Long parentId;

    @Builder.Default
    Boolean isActive = true;

    @Builder.Default
    Integer sortOrder = 0;
    String imageUrl;
}
