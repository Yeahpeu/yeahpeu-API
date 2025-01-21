package com.yeahpeu.user.service;

import com.yeahpeu.common.exception.NotFoundException;
import com.yeahpeu.user.entity.UserEntity;
import com.yeahpeu.user.service.dto.UserDTO;
import com.yeahpeu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(UserDTO::fromEntity).toList();
    }

    @Override
    public UserDTO getMe(Long userId) {
        return userRepository.findById(userId).map(UserDTO::fromEntity).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }
}
