package com.yeahpeu.wishlist.repository;

import com.yeahpeu.wishlist.domain.WishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {

    //    @Query("SELECT w FROM WishlistEntity w WHERE w.wedding.id = :weddingId")
    Optional<WishlistEntity> findByWedding_Id(Long weddingId);
}
