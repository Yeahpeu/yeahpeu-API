package com.yeahpeu.auth.controller.response;

import com.yeahpeu.auth.servcie.dto.CreateVerificationCodeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateVerificationCodeResponse {
    private Boolean verificationCodeCreated;
    private Boolean authStatus;

    public static CreateVerificationCodeResponse from(CreateVerificationCodeDTO dto) {
        return new CreateVerificationCodeResponse(
                dto.getVerificationCodeCreated(),
                dto.getAuthStatus()
        );
    }
}
