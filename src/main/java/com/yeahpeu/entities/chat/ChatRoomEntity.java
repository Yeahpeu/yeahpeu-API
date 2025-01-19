package com.yeahpeu.entities.chat;

import com.yeahpeu.entities.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chat_room")
public class ChatRoomEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "reservered_member_count", nullable = false)
    private Integer reservedMemberCount;

    @Column(name = "used_member_count", nullable = false)
    private Integer usedMemberCount;
}
