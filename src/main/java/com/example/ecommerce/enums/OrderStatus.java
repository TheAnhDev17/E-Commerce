package com.example.ecommerce.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    PROCESSING("Đang chuẩn bị"),
    SHIPPED("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Đã hủy"),
    RETURNED("Đã trả hàng"),
    REFUNDED("Đã hoàn tiền")
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
