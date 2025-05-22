package com.yeahpeu.wishlist.repository;

import com.yeahpeu.wishlist.domain.WishItemEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishItemRepository extends JpaRepository<WishItemEntity, Long>, WishItemCustomRepository {
    Optional<Long> countByWishlistId(Long weddingId);

    @Query("SELECT wI FROM WishItemEntity wI WHERE wI.wishlist.id = :wishlistId AND wI.naverProductId = :naverProductId")
    Optional<WishItemEntity> findByWishlist_IdAndNaverProductId(@Param("wishlistId") Long wishlistId,
                                                                @Param("naverProductId") Long naverProductId);
}
