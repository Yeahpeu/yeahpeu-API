package com.yeahpeu.wishlist.service.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NaverShoppingCommand {
    String keyword;
    int page;

    public static NaverShoppingCommand of(String keyword, int page) {
        return new NaverShoppingCommand(keyword, page);
    }
}
