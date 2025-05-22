package com.yeahpeu.wishlist.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.wedding.domain.WeddingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WeddingEntity wedding;

    @Column
    private String name;

    @OneToMany(mappedBy = "wishlist", fetch = LAZY)
    private List<WishItemEntity> wishItemEntities = new ArrayList<>();
}
