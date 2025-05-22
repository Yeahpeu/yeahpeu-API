package com.yeahpeu.wishlist.repository;

import com.yeahpeu.wishlist.domain.WishItemEntity;

import java.util.List;
import java.util.Optional;

public interface WishItemCustomRepository {
    Optional<List<WishItemEntity>> findWishItems(Long wishlistId, Integer size);
}
