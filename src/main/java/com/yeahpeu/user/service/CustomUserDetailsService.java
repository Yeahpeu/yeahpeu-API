package com.yeahpeu.user.service;


import com.yeahpeu.user.dto.CustomUserDetails;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // db 에서 특정 유저 조회하여 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElse(null);

        System.out.println("userEntity = " + userEntity);

        if (userEntity != null) {
            return new CustomUserDetails(userEntity);
        }
        return null;
    }

//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        UserEntity user = userRepository.findUserWithRole(Long.valueOf(userId))
//                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
//
//        return new UserPrincipal(user.getId().toString(), user.getPassword(), user.getRole());
//    }
}

