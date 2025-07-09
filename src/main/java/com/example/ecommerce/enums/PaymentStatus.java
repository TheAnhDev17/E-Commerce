package com.example.ecommerce.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("Chờ thanh toán"),
    PAID("Đã thanh toán"),
    FAILED("Thanh toán thất bại"),
    REFUNDED("Đã hoàn tiền"),
    PARTIALLY_REFUNDED("Hoàn tiền một phần"),
    CANCELLED("Đã hủy");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
