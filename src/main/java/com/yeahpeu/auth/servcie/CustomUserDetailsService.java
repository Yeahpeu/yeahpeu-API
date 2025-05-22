package com.yeahpeu.auth.servcie;

import com.yeahpeu.auth.domain.UserPrincipal;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new UserPrincipal(user.getId().toString(), user.getPassword(), user.getEmailAddress());
    }
}
