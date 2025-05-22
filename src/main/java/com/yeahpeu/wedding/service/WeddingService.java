package com.yeahpeu.wedding.service;

import com.yeahpeu.wedding.service.command.JoinWeddingCommand;
import com.yeahpeu.wedding.service.command.OnboardingCommand;
import com.yeahpeu.wedding.service.dto.BoardingCheckDto;

public interface WeddingService {

    void processOnboarding(OnboardingCommand command);

    void joinWedding(JoinWeddingCommand command);

    BoardingCheckDto isOnboarded(Long userId);
}
