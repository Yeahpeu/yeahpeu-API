package com.yeahpeu.user.repository;


import com.yeahpeu.user.entity.RoleEntity;
import com.yeahpeu.user.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(RoleType role);
}
