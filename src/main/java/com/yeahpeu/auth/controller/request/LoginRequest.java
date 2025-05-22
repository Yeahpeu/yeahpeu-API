package com.yeahpeu.auth.controller.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {
    private String emailAddress;
    private String password;
}
