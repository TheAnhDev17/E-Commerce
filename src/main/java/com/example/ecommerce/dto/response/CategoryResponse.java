package com.example.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    Long id;
    String name;
    String slug;
    String description;
    Long parentId;

    @Builder.Default
    Integer sortOrder = 0;
    String imageUrl;
}
