package com.yeahpeu.chat.domain;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ChatMessageId implements Serializable {
    private Long messageId;
    private Long roomId;

    public ChatMessageId() {
    }

    public ChatMessageId(Long messageId, Long roomId) {
        this.messageId = messageId;
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessageId that = (ChatMessageId) o;

        if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null) return false;
        return roomId != null ? roomId.equals(that.roomId) : that.roomId == null;
    }

    @Override
    public int hashCode() {
        int result = messageId != null ? messageId.hashCode() : 0;
        result = 31 * result + (roomId != null ? roomId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChatMessageId{" +
                "messageId='" + messageId + '\'' +
                ", roomId='" + roomId + '\'' +
                '}';
    }
}
