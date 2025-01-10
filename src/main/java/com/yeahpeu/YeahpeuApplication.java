package com.yeahpeu;

import com.yeahpeu.user.entity.RoleEntity;
import com.yeahpeu.user.entity.RoleType;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.repository.RoleRepository;
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
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            try {
                RoleEntity roleUser = new RoleEntity();
                roleUser.setRole(RoleType.USER);
                roleRepository.save(roleUser);
            } catch (Exception e) {
                System.out.println("Role already exists");
            }

            try {
                RoleEntity roleAdmin = new RoleEntity();
                roleAdmin.setRole(RoleType.ADMIN);
                roleRepository.save(roleAdmin);
            } catch (Exception e) {
                System.out.println("Role already exists");
            }

            List<RoleEntity> roles = roleRepository.findAll();

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
