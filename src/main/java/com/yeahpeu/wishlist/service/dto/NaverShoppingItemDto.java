package com.yeahpeu.wishlist.service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NaverShoppingItemDto {

    @JsonAlias("productId")
    private long naverProductId;

    private String title;

    @JsonAlias("image")
    private String imageUrl;

    @JsonAlias("lprice")
    private int price;

    @JsonAlias("link")
    private String linkUrl;

    private String mallName;
}
