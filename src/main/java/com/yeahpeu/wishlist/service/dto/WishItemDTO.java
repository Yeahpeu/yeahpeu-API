package com.yeahpeu.wishlist.service.dto;

import com.yeahpeu.wishlist.domain.WishItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishItemDTO {

    private Long id;
    private long naverProductId;
    private String title;
    private String imageUrl;
    private int price;
    private String linkUrl;
    private String mallName;

    static public WishItemDTO from(WishItemEntity entity) {
        return new WishItemDTO(
                entity.getId(),
                entity.getNaverProductId(),
                entity.getTitle(),
                entity.getImageUrl(),
                entity.getPrice(),
                entity.getLinkUrl(),
                entity.getMallName()
        );
    }

}
