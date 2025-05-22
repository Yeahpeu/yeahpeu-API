package com.yeahpeu.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "email_auth")
public class EmailAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @Column(name = "auth_code", nullable = false)
    private String authCode;

    @Column(name = "expired_at", nullable = false)
    private ZonedDateTime expiredAt;

    @Column(name = "auth_status", nullable = false)
    private Boolean authStatus;
}
