package com.yeahpeu.chat.servcie;

import com.yeahpeu.chat.servcie.command.SendMessageCommand;
import com.yeahpeu.chat.servcie.dto.ChatRoomMessageDTO;

public interface ChatMessageService {
    ChatRoomMessageDTO sendMessage(SendMessageCommand command);
}
