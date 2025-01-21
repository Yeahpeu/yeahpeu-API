package com.yeahpeu;

import com.yeahpeu.user.entity.RoleType;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class YeahpeuApplication {

    public static void main(String[] args) {
        SpringApplication.run(YeahpeuApplication.class, args);
    }


    @Bean
    public CommandLineRunner init(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // User 10명 생성
            for (int i = 0; i < 10; i++) {
                UserEntity user = new UserEntity();
                user.setEmailAddress("test" + i + "@test.com");
                user.setPassword(passwordEncoder.encode("password"));
                user.setNickname("test" + i);
                user.setRole(RoleType.USER);
                userRepository.save(user);
            }

        };
    }
}
