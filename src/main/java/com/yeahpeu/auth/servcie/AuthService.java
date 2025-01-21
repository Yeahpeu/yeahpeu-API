package com.yeahpeu.auth.servcie;

import com.yeahpeu.auth.servcie.dto.AuthTokenDTO;

public interface AuthService {
    AuthTokenDTO signup(String emailAddress, String password, String nickName);
    AuthTokenDTO login(String emailAddress, String password);
    AuthTokenDTO reissueTokens(String token);
}
