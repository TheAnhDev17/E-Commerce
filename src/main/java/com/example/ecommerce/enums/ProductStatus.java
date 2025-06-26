package com.example.ecommerce.enums;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ProductStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    DRAFT("draft");

    private final String value;

    ProductStatus(String value){
        this.value = value;
    }
}
