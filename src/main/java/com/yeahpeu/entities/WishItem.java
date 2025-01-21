package com.yeahpeu.entities;

import com.yeahpeu.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "wish_item")
public class WishItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wishlist_id", nullable = false)
    private WishlistEntity wishlist;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "link_url", nullable = false)
    private String linkUrl;

    @Column(name = "maker", nullable = true)
    private String maker;
}
