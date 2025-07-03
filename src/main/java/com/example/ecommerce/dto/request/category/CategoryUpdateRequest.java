package com.example.ecommerce.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateRequest {

    @NotBlank(message = "CATEGORY_NAME_REQUIRED")
    String name;
    String description;
    Long parentId;
    Boolean isActive;
    Integer sortOrder;
    String imageUrl;
}
