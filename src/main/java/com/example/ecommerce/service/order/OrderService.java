package com.example.ecommerce.service.order;

import com.example.ecommerce.dto.request.order.CreateOrderItemRequest;
import com.example.ecommerce.dto.request.order.CreateOrderRequest;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentStatus;
import com.example.ecommerce.exception.cart.CartErrorCode;
import com.example.ecommerce.exception.cart.CartException;
import com.example.ecommerce.exception.product.ProductErrorCode;
import com.example.ecommerce.exception.product.ProductException;
import com.example.ecommerce.exception.user.UserErrorCode;
import com.example.ecommerce.exception.user.UserException;
import com.example.ecommerce.mapper.order.OrderItemMapper;
import com.example.ecommerce.mapper.order.OrderMapper;
import com.example.ecommerce.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    OrderItemRepository orderItemRepository;
    OrderRepository orderRepository;
    CartItemRepository cartItemRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ProductVariantRepository productVariantRepository;
    OrderMapper orderMapper;
    OrderItemMapper orderItemMapper;
    static SecureRandom secureRandom = new SecureRandom();


    public Order createOrder(String userId, CreateOrderRequest request) {
        log.info("Creating order for user: {} with {} items", userId, request.getOrderItems().size());

        // 1. Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXISTED));

        // 2. Validate products and inventory
        validateOrderItem(request.getOrderItems());

        // 3. Create order
        Order order = orderMapper.toOrder(request);
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);

        // 4. Save order first to get ID
        order = orderRepository.save(order);

        // 5. Create order items form request
        List<OrderItem> orderItems = createOrderItemsFromRequest(order, request.getOrderItems());
        order.setOrderItems(orderItems);

        // 6. Calculate totals


        // 7. Apply discount if coupon provided

        // 8. Calculate shipping fee

        // 9. Final calculation

        // 10. Update inventory

        // 11. Save final order



        return  order;
    }

    // Helper method
    private void validateOrderItem(List<CreateOrderItemRequest> orderItemRequests){
       for (CreateOrderItemRequest itemRequest : orderItemRequests) {
           // Validate product exists
           Product product = productRepository.findById(itemRequest.getProductId())
                   .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

           // Validate variant if specified
           ProductVariant variant = null;
           if (itemRequest.getProductVariantId() != null) {
               variant = productVariantRepository.findById(itemRequest.getProductVariantId())
                       .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_VARIANT_NOT_FOUND));

               // Ensure variant belongs to the product
               if (!variant.getProduct().getId().equals(product.getId())) {
                   throw  new ProductException(ProductErrorCode.PRODUCT_VARIANT_NOT_BELONG_TO_PRODUCT);
               }
           }

           // Check inventory
            if (variant != null) {
                if (variant.getStockQuantity() < itemRequest.getQuantity()) {
                    throw new ProductException(ProductErrorCode.PRODUCT_VARIANT_INSUFFICIENT_STOCK);
                }
            } else {
                if (product.getStockQuantity() < itemRequest.getQuantity()) {
                    throw new ProductException(ProductErrorCode.PRODUCT_VARIANT_INSUFFICIENT_STOCK);
                }
            }

       }
    }

    private String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();

        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timePart = now.format(DateTimeFormatter.ofPattern("HHmmss"));

        String randomPart = String.format("%03d", secureRandom.nextInt(1000));

        return String.format("ORD-%s-%s-%s", datePart, timePart, randomPart);
    }

    private List<OrderItem> createOrderItemsFromRequest(Order order, List<CreateOrderItemRequest> orderItemRequests) {
        List<OrderItem> orderItems =  new ArrayList<>();

        for (CreateOrderItemRequest itemRequest : orderItemRequests) {
            // Validate product exists
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_NOT_FOUND));

            ProductVariant variant = null;
            if (itemRequest.getProductVariantId() != null) {
                variant = productVariantRepository.findById(itemRequest.getProductVariantId())
                        .orElseThrow(() -> new ProductException(ProductErrorCode.PRODUCT_VARIANT_NOT_FOUND));

                // Ensure variant belongs to the product
                if (!variant.getProduct().getId().equals(product.getId())) {
                    throw  new ProductException(ProductErrorCode.PRODUCT_VARIANT_NOT_BELONG_TO_PRODUCT);
                }
            }

            OrderItem orderItem = orderItemMapper.toOrder(itemRequest);
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductVariant(variant);

            // Set price - use variant price if available, otherwise product price
            if (variant != null && variant.getPrice() != null) {
                orderItem.setUnitPrice(variant.getPrice());
                orderItem.setOriginalPrice(variant.getComparePrice() != null ? variant.getComparePrice() : variant.getPrice());
            } else {
                orderItem.setUnitPrice(product.getPrice());
                orderItem.setOriginalPrice(product.getComparePrice());
            }

            // Product snapshot
            orderItem.setProductName(product.getName());
            orderItem.setProductSku(product.getSku());

            if (variant != null) {
                orderItem.setVariantName(variant.getNameSuffix());
                orderItem.setVariantSku(variant.getSku());
            }

            // Set product image
            if (!product.getImages().isEmpty()) {
                orderItem.setProductImageUrl(product.getImages().stream().findFirst().get().getImageUrl());
            }


            // Set weight and dimensions if available
            if (variant !=  null) {
                orderItem.setWeight(variant.getWeight());
                orderItem.setDimensions(variant.getDimensions());
            } else {
                orderItem.setWeight(product.getWeight());
                orderItem.setDimensions(product.getDimensions());
            }

            // Calculate subtotal
            orderItem.calculateSubtotal();

            orderItems.add(orderItemRepository.save(orderItem));
        }

        return orderItems;
    }
}
