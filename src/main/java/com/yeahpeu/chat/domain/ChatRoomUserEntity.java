package com.yeahpeu.chat.domain;


import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
@Entity
@Table(
        name = "chat_room_user",
        indexes = {
                @Index(name = "idx_user_room", columnList = "user_id, chat_room_id", unique = true)
        }
)
public class ChatRoomUserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoom;

    @Column(name = "last_read_message_id", nullable = false)
    private Long lastReadMessageId;
}