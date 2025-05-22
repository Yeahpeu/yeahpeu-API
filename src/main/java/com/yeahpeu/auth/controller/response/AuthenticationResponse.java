package com.yeahpeu.auth.controller.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class AuthenticationResponse {
    private String accessToken;
}
