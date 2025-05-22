package com.yeahpeu.user.service;


import com.yeahpeu.user.controller.request.UserRequest;
import com.yeahpeu.user.service.command.UpdateUserProfileCommand;
import com.yeahpeu.user.service.dto.UserDTO;
import com.yeahpeu.user.service.dto.UserProfileDTO;
import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getMe(Long userId);

    UserDTO addUser(UserRequest body);

    UserProfileDTO getMyProfile(Long userId);

    UserProfileDTO updateMyProfile(UpdateUserProfileCommand command);

    void deleteUser(Long userId);

    UserProfileDTO getOpponent(String opponentCode);
}
