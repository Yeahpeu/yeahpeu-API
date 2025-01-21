package com.yeahpeu.user.entity;

import com.yeahpeu.entities.WeddingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email_address", nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = true)
    private String myCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", nullable = true)
    private WeddingEntity wedding;

    @Column(nullable = true)
    private String provider;

    @Column(nullable = true)
    private String providerId;

    @Enumerated(EnumType.STRING)
    private RoleType role;

}
