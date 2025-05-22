package com.yeahpeu.wishlist.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WishItemRequest {
    private long naverProductId;
    private String title;
    private String imageUrl;
    private int price;
    private String linkUrl;
    private String mallName;
}
