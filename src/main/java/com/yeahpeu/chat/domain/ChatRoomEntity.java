package com.yeahpeu.chat.domain;

import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoomEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "reserved_member_count", nullable = false)
    private Integer reservedMemberCount; // 오타 수정

    @Column(name = "used_member_count", nullable = false)
    private Integer usedMemberCount;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // optional = false로 명시
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity creator; // 방 생성자 (UserEntity와 연관)
}
