package com.example.ecommerce.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {

    COD("Thanh toán khi nhận hàng"),
    VNPAY("VNPAY"),
    MOMO("Ví MoMo"),
    BANKING("Internet Banking"),
    ZALOPAY("ZaloPay"),
    SHOPEE_PAY("ShopeePay"),
    CREDIT_CARD("Thẻ tín dụng"),
    BANK_TRANSFER("Chuyển khoản ngân hàng")
    ;

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}
