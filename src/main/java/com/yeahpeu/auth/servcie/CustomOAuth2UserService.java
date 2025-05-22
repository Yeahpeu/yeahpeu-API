package com.yeahpeu.auth.servcie;


import com.yeahpeu.auth.servcie.dto.oauth2dto.CustomOAuth2User;
import com.yeahpeu.auth.servcie.dto.oauth2dto.GoogleDto;
import com.yeahpeu.auth.servcie.dto.oauth2dto.OAuth2Dto;
import com.yeahpeu.auth.util.JwtUtil;
import com.yeahpeu.user.entity.RoleType;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import com.yeahpeu.user.util.AuthCodeUtil;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * 소셜 로그인 이후 가져온 사용자의 정보들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능 지원하는 클래스 구글로그인 이후 전달해주는 정보를 우리 DB 에 저장
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CustomOAuth2UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //부모클래스의 로드유저 메서드에서 (third party 에서 추출된) 유저정보를 가져옵니다
        OAuth2User oAuth2User = super.loadUser(userRequest);
        //System.out.println(oAuth2User.getAuthorities());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //서로 DTO 가 다르기 때문에 다르게 받아줘야 합니다.
        OAuth2Dto oAuth2Dto = switch (registrationId) {
            //구글의 경우 result-code, message, id, name 을 담습니다.
            case "google" -> new GoogleDto(oAuth2User.getAttributes());
            // 확장시
            //case "naver" -> new NaverDto(oAuth2User.getAttributes());
            //case "kakao" -> new KakaoDto(oAuth2User.getAttributes());
            //case "admin" -> new AdminDto(oAuth2User.getAttributes());
            default -> null;
        };

        if (oAuth2Dto == null) {
            return null;
        } else {
            return handleUser(oAuth2Dto);
        }
    }


    public OAuth2User handleUser(OAuth2Dto oAuth2Dto) throws OAuth2AuthenticationException {

        //third party 로그인 정보를 우리 Database에 저장하기 위한 코드입니다.
        //String userId = oAuth2Dto.getProvider()+" "+ oAuth2Dto.getProviderId();
        String email = oAuth2Dto.getEmail();

        // 이미 유저가 존재하는지, 우리는 provider 로 만든 정보를 emailAddress로 검증 및 관리.
        UserEntity entity = userRepository.findByEmailAddress(email).orElse(null);

        // 2. User 생성
        if (entity == null) {
            //신규 유저인 경우
            UserEntity userEntity = new UserEntity();
            userEntity.setEmailAddress(oAuth2Dto.getEmail());
            userEntity.setNickname(oAuth2Dto.getName());
            userEntity.setName(oAuth2Dto.getName());
            userEntity.setRole(RoleType.USER);
            userEntity.setProvider(oAuth2Dto.getProvider());
            userEntity.setMyCode(AuthCodeUtil.generateCode());
            userEntity.setProviderId(oAuth2Dto.getProviderId());
            userEntity.setAvatarUrl(oAuth2Dto.getPicture());
            entity = userEntity;
        } else {
            // 기존 유저인 경우 유저 업데이트
            entity.setEmailAddress(oAuth2Dto.getEmail());
        }
        // 자체 DB 에 저장
        UserEntity saved = userRepository.save(entity); // auto create 한 id 값 추출

        return new CustomOAuth2User(saved);
    }

}
