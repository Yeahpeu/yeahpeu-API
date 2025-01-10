package com.yeahpeu.auth.service;


import com.yeahpeu.auth.dto.CustomOAuth2User;
import com.yeahpeu.auth.dto.OAuth2Response;
import com.yeahpeu.auth.dto.OAuthAttributes;
import com.yeahpeu.auth.dto.oauth2response.GoogleResponse;
import com.yeahpeu.user.entity.RoleType;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * 소셜 로그인 이후 가져온 사용자의 정보들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능 지원하는 클래스
 * 구글로그인 이후 전달해주는 정보를
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository, HttpSession httpSession) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //부모클래스의 로드유저메서드에서 유저정보를 가져옵니다
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAuthorities());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //서로 DTO 가 다르기 때문에 다르게 받아줘야 합니다.
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {
            //구글의 경우 result-code, message, id, name 을 담습니다.
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {

        } else {
            //todo
            return null;
        }

        //third party 로그인정보를 우리 Database에 저장하기 위한 코드입니다.
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        String role = null;
        UserEntity entity = userRepository.findByUsername(username).orElse(null);

        if(entity== null){
            //신규 유저인 경우
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmailAddress(oAuth2Response.getEmail());
            userEntity.setRole(RoleType.USER);
            userRepository.save(userEntity);
        } else {
            //새로운 유저인 경우 유저 업데이트
            role = entity.getRole().toString();
            entity.setEmailAddress(oAuth2Response.getEmail());
            userRepository.save(entity);
        }


        //우리가 만든 customOauth2 유저 서비스를 시큐리티컨피그에 등록해야 합니다.
        role = "ROLE_USER";
        return new CustomOAuth2User(oAuth2Response, role);

    }

    private UserEntity saveOrUpdate(OAuthAttributes attributes) {
        UserEntity user = userRepository.findByEmailAddress(attributes.getEmail())
                .orElse(attributes.toEntity());
        return userRepository.save(user);

    }

}