package com.yeahpeu.auth.repository;

import com.yeahpeu.auth.domain.EmailAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuthEntity, Long> {
    EmailAuthEntity findByEmailAddress(String Email);
}
