package com.yeahpeu.entities;

import com.yeahpeu.entities.common.BaseEntity;
import com.yeahpeu.user.entity.UserEntity;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "wedding")
public class WeddingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "bride_id", nullable = false)
    private UserEntity bride;

    @OneToOne
    @JoinColumn(name = "groom_id", nullable = false)
    private UserEntity groom;

    @Column(name = "wedding_day", nullable = false)
    private ZonedDateTime weddingDay;

    @Column(name = "is_onboarded", nullable = false)
    private Boolean isOnboarded;
}
