package com.yeahpeu.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Table(name = "users")
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
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
    private String provider;

    @Column(nullable = true)
    private String providerId;

//    @Enumerated(EnumType.STRING) 이미 roles로 관리하고 있음 지웁니당
//    private RoleType role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<UserRoleEntity> roles = new HashSet<>();

    public UserEntity() {

    }

//    public void addRole(RoleEntity role) {
//        UserRoleEntity userRoleEntity = new UserRoleEntity();
//        userRoleEntity.setUser(this);
//        userRoleEntity.setRole(role);
//        this.roles.add(userRoleEntity);
//    }

}
