package com.yeahpeu.user.repository;


import com.yeahpeu.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByName(String username);

    Optional<UserEntity> findByName(String username);

    @Query("""
        SELECT u FROM UserEntity u
        WHERE u.emailAddress = :emailAddress
    """)
    Optional<UserEntity> findByEmailAddress(@Param("emailAddress") String emailAddress);

//    // user의 UserRoleEntity와 RoleEntity를 한번에 페치 조인
//    @Query("""
//        SELECT u FROM UserEntity u
//        JOIN FETCH u.role r
//        JOIN FETCH r.role
//        WHERE u.id = :id
//    """)
//    Optional<UserEntity> findUserWithRole(@Param("id") Long id);

    @Query("SELECT u FROM UserEntity u WHERE u.id IN :ids")
    List<UserEntity> findByIdIn(List<Long> ids);
}
