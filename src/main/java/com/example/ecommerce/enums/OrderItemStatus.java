package com.example.ecommerce.enums;

import lombok.Getter;

@Getter
public enum OrderItemStatus {
    PENDING("Chờ xử lý"),
    CONFIRMED("Đã xác nhận"),
    PROCESSING("Đang chuẩn bị"),
    SHIPPED("Đang giao hàng"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Đã hủy"),
    RETURNED("Đã trả hàng"),
    REFUNDED("Đã hoàn tiền"),
    OUT_OF_STOCK("Hết hàng"),
    BACKORDERED("Đặt hàng trước");

    private final String description;

    OrderItemStatus(String description) {
        this.description = description;
    }
}
