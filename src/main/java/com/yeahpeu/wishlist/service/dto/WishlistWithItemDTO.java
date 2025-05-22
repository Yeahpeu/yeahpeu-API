package com.yeahpeu.wishlist.service.dto;

import com.yeahpeu.wishlist.domain.WishItemEntity;
import com.yeahpeu.wishlist.domain.WishlistEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishlistWithItemDTO {

    private Long id;
    private String name;
    private Long total;
    private List<WishItemDTO> wishItemEntities = new ArrayList<>();

    static public WishlistWithItemDTO from(WishlistEntity wishlistEntity,
                                           List<WishItemEntity> wishItemEntity,
                                           Long total) {
        List<WishItemDTO> wishItems = wishItemEntity.stream()
                .map(WishItemDTO::from)  // 위에서 만든 DTO 변환 메서드 사용
                .collect(Collectors.toList());

        return new WishlistWithItemDTO(
                wishlistEntity.getId(),
                wishlistEntity.getName(),
                total,
                wishItems
        );
    }
}
