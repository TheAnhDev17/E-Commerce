package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.response.wishlist.WishlistItemResponse;
import com.example.ecommerce.dto.response.wishlist.WishlistResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductImage;
import com.example.ecommerce.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WishlistMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productSlug", source = "product.slug")
    @Mapping(target = "productPrice", source = "product.price")
    @Mapping(target = "productComparePrice", source = "product.comparePrice")
    @Mapping(target = "productImageUrl", expression = "java(getFirstProductImage(wishlist.getProduct()))")
    @Mapping(target = "isInStock", expression = "java(checkStock(wishlist))")
    @Mapping(target = "isOnSale", expression = "java(checkSale(wishlist))")
    @Mapping(target = "hasVariants", expression = "java(checkVariants(wishlist))")
    @Mapping(target = "addedAt", source = "createdAt")
    WishlistItemResponse toWishlistItemResponse(Wishlist wishlist);

    List<WishlistItemResponse> toWishlistItemResponses(List<Wishlist> wishlists);


    default WishlistResponse toWishlistResponse(List<Wishlist> wishlists) {
        if (wishlists == null) {
            return WishlistResponse.builder()
                    .items(Collections.emptyList())
                    .totalItems(0)
                    .lastUpdated(LocalDateTime.now())
                    .isEmpty(true)
                    .build();
        }

        List<WishlistItemResponse> itemResponses = toWishlistItemResponses(wishlists);

        return WishlistResponse.builder()
                .items(itemResponses)
                .totalItems(wishlists.size())
                .lastUpdated(LocalDateTime.now())
                .isEmpty(wishlists.isEmpty())
                .build();
    }

    default String getFirstProductImage(Product product){
        if (product == null || product.getImages() == null || product.getImages().isEmpty()) {
            return  null;
        }

        return product.getImages().stream()
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(null);
    }

    default Boolean checkStock(Wishlist wishlist) {
        if (wishlist.getProduct() != null ) {
            return wishlist.getProduct().getStockQuantity() > 0;
        }

        return false;
    }

    default Boolean checkSale(Wishlist wishlist) {
        return false;
    }

    default Boolean checkVariants(Wishlist wishlist) {
        if (wishlist.getProduct() != null ) {
            return wishlist.getProduct().hasVariants();
        }

        return false;
    }
}
