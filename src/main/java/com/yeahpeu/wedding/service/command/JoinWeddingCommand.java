package com.yeahpeu.wedding.service.command;

import com.yeahpeu.wedding.controller.request.JoinWeddingRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinWeddingCommand {
    private Long userId;
    private String partnerCode;

    public static JoinWeddingCommand from(Long userId, JoinWeddingRequest body) {
        return new JoinWeddingCommand(
                userId,
                body.getPartnerCode()
        );
    }
}
