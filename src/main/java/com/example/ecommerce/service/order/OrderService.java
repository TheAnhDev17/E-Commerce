package com.example.ecommerce.service.order;

import com.example.ecommerce.dto.request.order.CreateOrderItemRequest;
import com.example.ecommerce.dto.request.order.CreateOrderRequest;
import com.example.ecommerce.dto.response.order.OrderResponse;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.enums.OrderItemStatus;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.enums.PaymentStatus;
import com.example.ecommerce.exception.cart.CartErrorCode;
import com.example.ecommerce.exception.cart.CartException;
import com.example.ecommerce.exception.order.OrderErrorCode;
import com.example.ecommerce.exception.order.OrderException;
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

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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


    /**
     * Create order from request (direct order creation)
     */

    public OrderResponse createOrder(String userId, CreateOrderRequest request) {
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
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setShippingFee(BigDecimal.ZERO);
        order.setSubtotal(BigDecimal.ZERO);
        order.setTaxAmount(BigDecimal.ZERO);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setOrderItems(null);

        // 4. Save order first to get ID
        order = orderRepository.save(order);

        // 5. Create order items form request
        List<OrderItem> orderItems = createOrderItemsFromRequest(order, request.getOrderItems());
        order.setOrderItems(orderItems);

        // 6. Calculate totals
        calculateOrderTotals(order);

        // 7. Apply discount if coupon provided
        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            applyDiscount(order, request.getCouponCode());
        }

        // 8. Calculate shipping fee
        calculateShippingFee(order);

        // 9. Final calculation
        order.calculateTotalAmount();

        // 10. Update inventory
        updateInventory(orderItems);

        // 11. Save final order
        return  orderMapper.toOrderResponse(orderRepository.save(order));
    }


    /**
     * Create order from cart
     */
    public OrderResponse createOrderFromCart(String userId, CreateOrderRequest request) {
        log.info("Creating order from cart for user: {}", userId);

        // 1. Validate user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXISTED));

        // 2. Get user's cart items directly
        List<CartItem> cartItems = cartItemRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (cartItems.isEmpty()) {
            throw new CartException(CartErrorCode.CART_IS_EMPTY);
        }

        // 3. Validate inventory
        validateCartItems(cartItems);


        // 4. Create order
        Order order = orderMapper.toOrder(request);
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentStatus(PaymentStatus.PENDING);

        // Initialize money fields to ZERO (prevent null constraint errors)
        order.setSubtotal(BigDecimal.ZERO);
        order.setShippingFee(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setTaxAmount(BigDecimal.ZERO);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setOrderItems(null);

        // 5. Save order first to get ID (WITHOUT OrderItems)
        order = orderRepository.save(order);

        // 6. Create order items from cart items AFTER getting order ID
        List<OrderItem> orderItems = createOrderItemsFromCart(order, cartItems);

        // 7. Set order items to order
        order.setOrderItems(orderItems);

        // 8. Calculate totals
        calculateOrderTotals(order);

        // 9. Apply discount if coupon provided
        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            applyDiscount(order, request.getCouponCode());
        }

        // 10. Calculate shipping Fee
        calculateShippingFee(order);

        // 11. Final calculate
        order.calculateTotalAmount();

        // 12. Update inventory
        updateInventory(orderItems);

        // 13. Clear cart
        cartItemRepository.deleteByUserId(userId);

        // 14. Save final order
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponse(savedOrder);
    }


    /**
     * Confirm order (admin action)
     */
    public OrderResponse confirmOrder(Long orderId) {
        log.info("Confirming order: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderException(OrderErrorCode.INVALID_ORDER_STATUS);
        }

        // Validate payment for non-COD orders
        if (order.getPaymentMethod() != PaymentMethod.COD && order.getPaymentStatus() != PaymentStatus.PAID) {
            throw  new OrderException(OrderErrorCode.PAYMENT_REQUIRED);
        }

        order.setStatus(OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());

        // Update all order items status
        order.getOrderItems().forEach(item -> item.setStatus(OrderItemStatus.CONFIRMED));

        Order savedOrder = orderRepository.save(order);

        log.info("Order confirmed: {}", order.getOrderNumber());

        return orderMapper.toOrderResponse(savedOrder);
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

            OrderItem orderItem = orderItemMapper.toOrderItem(itemRequest);
            log.debug("After mapper: discountAmount={}", orderItem.getDiscountAmount());

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductVariant(variant);
            orderItem.setStatus(OrderItemStatus.PENDING);
            orderItem.setDiscountAmount(BigDecimal.ZERO);

            log.debug("After setDiscountAmount: discountAmount={}", orderItem.getDiscountAmount());

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

            // Before save
            log.debug("Before save: {}", orderItem);
            orderItems.add(orderItemRepository.save(orderItem));
        }

        return orderItems;
    }

    /**
     * Calculate order totals
     */
    public void calculateOrderTotals(Order order) {
        // Calculate subtotal from order items
        order.calculateSubtotal();

        // Calculate tax (if applicable)
        BigDecimal taxRate = new BigDecimal("0.10"); // 10% VAT
        order.setTaxAmount(order.getSubtotal().multiply(taxRate));

        // Final calculation
        order.calculateTotalAmount();
    }


    /**
     * Apply discount using coupon
     */
    private void applyDiscount(Order order, String couponCode) {
        if (couponCode != null && !couponCode.trim().isEmpty()) {
            BigDecimal discountPercent = new BigDecimal("0.10");
            BigDecimal discountAmount = order.getSubtotal().multiply(discountPercent);

            order.setDiscountAmount(discountAmount);
            order.setCouponCode(couponCode);
        }
    }

    private void calculateShippingFee(Order order) {
        BigDecimal baseShippingFee = new BigDecimal("30000");

        // Free shipping for orders over 500k
        if (order.getSubtotal().compareTo(new BigDecimal("500000")) >= 0) {
            order.setShippingFee(BigDecimal.ZERO);
        } else {
            order.setShippingFee(baseShippingFee);
        }
    }

    private void updateInventory(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProductVariant() != null) {
                ProductVariant variant = orderItem.getProductVariant();
                variant.setStockQuantity(variant.getStockQuantity() - orderItem.getQuantity());
                productVariantRepository.save(variant);
            } else {
                Product product = orderItem.getProduct();
                product.setStockQuantity(product.getStockQuantity() - orderItem.getQuantity());
                productRepository.save(product);
            }
        }
    }

    private void validateCartItems(List<CartItem> cartItems) {
        for (CartItem cartItem: cartItems) {
            if (cartItem.getProductVariant() != null) {
                ProductVariant variant = cartItem.getProductVariant();
                if (variant.getStockQuantity() < cartItem.getQuantity()) {
                    throw new ProductException(ProductErrorCode.PRODUCT_VARIANT_INSUFFICIENT_STOCK);
                }
            } else {
                Product product = cartItem.getProduct();
                if (product.getStockQuantity() < cartItem.getQuantity()) {
                    throw new ProductException(ProductErrorCode.PRODUCT_VARIANT_INSUFFICIENT_STOCK);
                }
            }
        }
    }

    private List<OrderItem> createOrderItemsFromCart(Order order, List<CartItem> cartItems) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setProductVariant(cartItem.getProductVariant());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getProductVariant() != null ?
                    cartItem.getProductVariant().getPrice() : cartItem.getProduct().getPrice());
            orderItem.setOriginalPrice(cartItem.getProductVariant() != null ?
                    cartItem.getProductVariant().getComparePrice() : cartItem.getProduct().getComparePrice());

            // Initialize discount amount to ZERO (prevent null constraint error)
            orderItem.setDiscountAmount(BigDecimal.ZERO);

            // Product get snapshot
            orderItem.setProductName(cartItem.getProduct().getName());
            orderItem.setProductSku(cartItem.getProduct().getSku());

            if (cartItem.getProductVariant() != null) {
                orderItem.setVariantName(cartItem.getProductVariant().getNameSuffix());
                orderItem.setVariantSku(cartItem.getProductVariant().getSku());
            }

            // Set product image
            if (cartItem.getProduct() != null && cartItem.getProduct().getImages() != null && !cartItem.getProduct().getImages().isEmpty()) {
                cartItem.getProduct().getImages().stream().findFirst()
                        .ifPresent(image -> orderItem.setProductImageUrl(image.getImageUrl()));

            }

            // Calculate subtotal
            orderItem.calculateSubtotal();
            orderItems.add(orderItemRepository.save(orderItem));
        }

        return orderItems;
    }
}
