package com.yeahpeu.wishlist.service.command;

import com.yeahpeu.wishlist.controller.request.WishItemRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishItemCommand {
    private Long userId;
    private Long naverProductId;
    private String title;
    private String imageUrl;
    private int price;
    private String linkUrl;
    private String mallName;

    public static WishItemCommand from(Long userId, WishItemRequest wishlistRequest) {
        return new WishItemCommand(
                userId,
                wishlistRequest.getNaverProductId(),
                wishlistRequest.getTitle(),
                wishlistRequest.getImageUrl(),
                wishlistRequest.getPrice(),
                wishlistRequest.getLinkUrl(),
                wishlistRequest.getMallName()
        );
    }
}
