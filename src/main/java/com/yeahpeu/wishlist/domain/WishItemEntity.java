package com.yeahpeu.wishlist.domain;

import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.wishlist.service.command.WishItemCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "wish_item")
public class WishItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long naverProductId;

    @ManyToOne
    @JoinColumn(name = "wishlist_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WishlistEntity wishlist;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "link_url", nullable = false)
    private String linkUrl;

    @Column(name = "mall_name", nullable = true)
    private String mallName;

    private WishItemEntity(Long naverProductId, WishlistEntity wishlist, String title, String imageUrl, int price,
                           String linkUrl, String mallName) {
        this.naverProductId = naverProductId;
        this.wishlist = wishlist;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.linkUrl = linkUrl;
        this.mallName = mallName;
    }

    public static WishItemEntity from(WishlistEntity wishlist, WishItemCommand wishItemCommand) {
        return new WishItemEntity(
                wishItemCommand.getNaverProductId(),
                wishlist,
                wishItemCommand.getTitle(),
                wishItemCommand.getImageUrl(),
                wishItemCommand.getPrice(),
                wishItemCommand.getLinkUrl(),
                wishItemCommand.getMallName()
        );
    }
}
