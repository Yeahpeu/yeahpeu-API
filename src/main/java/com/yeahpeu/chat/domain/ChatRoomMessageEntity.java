package com.yeahpeu.chat.domain;

import com.yeahpeu.chat.service.command.MessageContentCommand;
import com.yeahpeu.common.domain.BaseEntity;
import com.yeahpeu.user.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Table(name = "chat_message")
@IdClass(ChatMessageId.class)
public class ChatRoomMessageEntity extends BaseEntity {

    @Id
    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @Id
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "text", nullable = false)
    private String text;

    // Self Reference
    @Column(name = "parent_message_id", nullable = true)
    private Long parentMessageId;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    // 메시지 발송 시각
    @Column(name = "sent_at", nullable = false)
    private ZonedDateTime sentAt;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageContentEntity> messageContent;

    public void setMessageContentFromCommands(List<MessageContentCommand> commands) {
        this.messageContent = commands.stream()
                .map(command -> MessageContentEntity.from(command, this))
                .toList();
    }
}
