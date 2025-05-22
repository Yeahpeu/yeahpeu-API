package com.yeahpeu.wishlist.service;

import com.yeahpeu.wishlist.service.command.NaverShoppingCommand;
import com.yeahpeu.wishlist.service.command.WishItemCommand;
import com.yeahpeu.wishlist.service.dto.NaverShoppingItemDto;
import com.yeahpeu.wishlist.service.dto.WishItemDTO;
import com.yeahpeu.wishlist.service.dto.WishlistWithItemDTO;
import java.util.List;

public interface WishlistService {
    // 쇼핑 검색 API
    List<NaverShoppingItemDto> getShoppingItems(NaverShoppingCommand command);

    WishItemDTO addWishItem(WishItemCommand wishItemCommand);

    WishlistWithItemDTO getWishlist(Long userId, Integer size);

    void deleteWishItem(Long wishItemId);
}
