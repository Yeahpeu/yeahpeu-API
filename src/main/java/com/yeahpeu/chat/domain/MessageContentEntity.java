package com.yeahpeu.chat.domain;

import com.yeahpeu.chat.service.command.MessageContentCommand;
import com.yeahpeu.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name = "chat_message_content")
public class MessageContentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type", nullable = false, columnDefinition = "varchar(200)")
    private String contentType;

    // S3 bucket key
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "message_id", referencedColumnName = "message_id"),
            @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    })
    private ChatRoomMessageEntity message;

    public static MessageContentEntity from(MessageContentCommand command, ChatRoomMessageEntity message) {
        MessageContentEntity entity = new MessageContentEntity();
        entity.setUrl(command.getUrl());
        entity.setContentType(command.getContentType());
        entity.setMessage(message);
        return entity;
    }
}
