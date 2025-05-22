package com.yeahpeu.chat.service;


import com.yeahpeu.chat.service.command.SendMessageCommand;
import com.yeahpeu.chat.service.dto.ChatRoomMessageDTO;

public interface ChatMessageService {
    ChatRoomMessageDTO sendMessage(SendMessageCommand command);
}
