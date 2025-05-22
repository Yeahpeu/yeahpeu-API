package com.yeahpeu.wishlist.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yeahpeu.wishlist.domain.WishItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.yeahpeu.wishlist.domain.QWishItemEntity.wishItemEntity;

@Repository
@RequiredArgsConstructor
public class WishItemRepositoryImpl implements WishItemCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<WishItemEntity>> findWishItems(Long wishlistId, Integer size) {
        List<WishItemEntity> wishItems = queryFactory
                .selectFrom(wishItemEntity)
                .where(wishItemEntity.wishlist.id.eq(wishlistId))
                .orderBy(wishItemEntity.createdAt.desc())
                .limit(size != null ? size : 20)
                .fetch();

        return Optional.ofNullable(wishItems);
    }
}
