package com.yeahpeu.user.entity;

import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.wedding.domain.WeddingEntity;
import com.yeahpeu.wedding.domain.WeddingRole;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@Entity
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = true)
    private String avatarUrl;

    @Column(nullable = true)
    private String myCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wedding_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = true)
    private WeddingEntity wedding;

    @Enumerated(EnumType.STRING)
    @Column(name = "wedding_role", columnDefinition = "varchar(255)")
    private WeddingRole weddingRole;

    @Column(nullable = true)
    private String provider;

    @Column(nullable = true)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(255)")
    private RoleType role;

    //고려사항
//    public static UserEntity from(UserRequest user) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setName(user.getName());
//        userEntity.setEmailAddress(user.getEmailAddress());
//        userEntity.setPassword(user.getPassword());
//        userEntity.setRole(RoleType.USER);
//        return userEntity;
//    }

}
