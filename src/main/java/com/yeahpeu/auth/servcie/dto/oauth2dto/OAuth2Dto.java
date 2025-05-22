package com.yeahpeu.auth.servcie.dto.oauth2dto;

public interface OAuth2Dto {

    //제공자이름 : 구글 등
    String getProvider();
    //제공자ID : 구글이 각 유저에게 부여한 번호
    String getProviderId();
    // 사용자이메일
    String getEmail();
    //사용자이름
    String getName();

    String getPicture();

}
