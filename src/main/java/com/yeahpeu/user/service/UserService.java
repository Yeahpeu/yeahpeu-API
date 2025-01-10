package com.yeahpeu.user.service;


import com.yeahpeu.user.dto.UserRequestDTO;
import com.yeahpeu.user.dto.UserResponseDTO;
import com.yeahpeu.user.entity.RoleType;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    // 유저 접근 권한 체크
    public Boolean isAccess(String username){
        //현재 로그인유저의 username // 스프링 시큐리티의 도움을 받아 ContextHolder 로 접근 가능하다
        String sessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        //현재 로그인유저의 role
        String sessionRole= SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        if("ROLE_ADMIN".equals(sessionRole)){
            return true;
        }

        if("ROLE_USER".equals(sessionUsername)){
            return true;
        }
        return false;
    }


    @Transactional
    public void createUser(UserRequestDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String nickname = dto.getNickname();
        String encodedPassword = passwordEncoder.encode(password);

        if(userRepository.existsByUsername(username)) {
            return;
        }

        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setNickname(nickname);
        entity.setPassword(encodedPassword);
        entity.setRole(RoleType.USER);
        userRepository.save(entity);
    }


    // 유저 한 명 읽기
    @Transactional
    public UserResponseDTO readOneUser(String username){
        UserEntity entity = userRepository.findByUsername(username).orElseThrow();

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUsername(entity.getUsername());
        dto.setRole(entity.getRole().toString());
        dto.setNickname(entity.getNickname());
        return dto;
    }


    // 스프링 시큐리티 전용 로그인 * 시큐리티 형식으로 반환
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByUsername(username).orElseThrow();

        return User.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .roles(entity.getRole().toString())
                .build();
    }
}
