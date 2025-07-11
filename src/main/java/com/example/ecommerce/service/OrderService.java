package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.order.CreateOrderRequest;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    CartItemRepository cartItemRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ProductVariantRepository productVariantRepository;

    public Order createOrderFromCart(Long userId, CreateOrderRequest request){

    }

}
