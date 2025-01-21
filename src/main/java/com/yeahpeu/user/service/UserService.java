package com.yeahpeu.user.service;

import com.yeahpeu.user.service.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getMe(Long userId);
}
