package com.yeahpeu.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    //, columnDefinition = "datetime(6) default current_timestamp(6)"
    // 위의 설정을 추가하면 MYSQL 전용으로 디폴트 설정 TEST에 h2를 사용하는 경우 사용 불가능
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @Column
    private ZonedDateTime updatedAt;

    @PrePersist
    public void PrePersist() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void PreUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

}
