package com.yeahpeu.auth.servcie;

import com.yeahpeu.auth.config.JwtUtil;
import com.yeahpeu.auth.servcie.dto.AuthTokenDTO;
import com.yeahpeu.common.exception.BadRequestException;
import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.common.exception.TokenException;
import com.yeahpeu.user.entity.RoleType;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthTokenDTO signup(String emailAddress, String password, String name) {

        // 1. Email 중복 체크
        userRepository.findByEmailAddress(emailAddress).ifPresent(user -> {
            throw new BadRequestException("Email already exists");
        });

        // 2. User 생성
        UserEntity userEntity = new UserEntity();
        userEntity.setEmailAddress(emailAddress);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setNickname(name);
        userEntity.setName(name);
        userEntity.setRole(RoleType.USER);

        UserEntity saved = userRepository.save(userEntity);

        // 4. Token 생성
        String accessToken = jwtUtil.createAccessToken(saved.getId());
        String refreshToken = jwtUtil.createRefreshToken(saved.getId());

        return new AuthTokenDTO(accessToken, refreshToken);
    }

    @Override
    public AuthTokenDTO login(String emailAddress, String password) {
        // 1. User 조회
        UserEntity userEntity = userRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // 2. Password 체크
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new RuntimeException("Password not matched");
        }

        // 3. Token 생성
        String accessToken = jwtUtil.createAccessToken(userEntity.getId());
        String refreshToken = jwtUtil.createRefreshToken(userEntity.getId());

        return new AuthTokenDTO(accessToken, refreshToken);
    }

    @Override
    public AuthTokenDTO reissueTokens(String token) {
        if (!jwtUtil.validateToken(token)){
            throw new TokenException("만료된 토큰입니다.");
        }
        Long userId = jwtUtil.getSubject(token);
        String accessToken = jwtUtil.createAccessToken(userId);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        return new AuthTokenDTO(accessToken, refreshToken);
    }
}
