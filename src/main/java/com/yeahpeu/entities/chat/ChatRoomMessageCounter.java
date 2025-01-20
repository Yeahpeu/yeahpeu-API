package com.yeahpeu.entities.chat;

import com.yeahpeu.entities.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "chat_room_message_counter",
        indexes = {
                @Index(name = "idx_room_id", columnList = "room_id", unique = true)
        }
)
public class ChatRoomMessageCounter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "last_message_id", nullable = false)
    private Long lastMessageId;

    // 읽기 전용 연관관계 설정
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumns({
            @JoinColumn(name = "room_id", referencedColumnName = "room_id", insertable = false, updatable = false),
            @JoinColumn(name = "last_message_id", referencedColumnName = "message_id", insertable = false, updatable = false)
    })
    @org.hibernate.annotations.NotFound(action = NotFoundAction.IGNORE)
    private ChatRoomMessageEntity lastMessage;

    public Long getNextMessageId() {
        return lastMessageId + 1;
    }
}
