package com.yeahpeu.entities;

import com.yeahpeu.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Table(name = "wishlist")
public class WishlistEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "wedding_id", nullable = false)
    private WeddingEntity wedding;

    @OneToMany(mappedBy = "wishlist", fetch = LAZY)
    private List<WishItem> wishItems = new ArrayList<>();
}
