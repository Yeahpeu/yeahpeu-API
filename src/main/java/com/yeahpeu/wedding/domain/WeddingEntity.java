package com.yeahpeu.wedding.domain;

import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Table(name = "wedding")
public class WeddingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bride_id", nullable = true)
    private UserEntity bride;

    @ManyToOne
    @JoinColumn(name = "groom_id", nullable = true)
    private UserEntity groom;

    @Column(name = "wedding_day", nullable = false)
    private ZonedDateTime weddingDay;

    @Column(name = "is_onboarded", nullable = false)
    private Boolean isOnboarded;
}
