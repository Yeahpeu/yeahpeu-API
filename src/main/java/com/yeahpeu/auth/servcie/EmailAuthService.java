package com.yeahpeu.auth.servcie;

public interface EmailAuthService {
    void createVerificationCode(String emailAddress);

    void validateAuthCode(String emailAddress, String authCode);
}
