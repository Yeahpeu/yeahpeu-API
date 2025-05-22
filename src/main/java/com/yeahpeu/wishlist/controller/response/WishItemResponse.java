package com.yeahpeu.wishlist.controller.response;

import com.yeahpeu.wishlist.service.dto.WishItemDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WishItemResponse {
    private Long id;

    private long naverProductId;

    private String title;

    private String imageUrl;

    private int price;

    private String linkUrl;

    private String mallName;

    public static WishItemResponse from(WishItemDTO dto) {
        return new WishItemResponse(
                dto.getId(),
                dto.getNaverProductId(),
                dto.getTitle(),
                dto.getImageUrl(),
                dto.getPrice(),
                dto.getLinkUrl(),
                dto.getMallName()
        );
    }
}
