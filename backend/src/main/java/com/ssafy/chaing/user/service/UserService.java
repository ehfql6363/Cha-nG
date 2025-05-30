package com.ssafy.chaing.user.service;

import com.ssafy.chaing.user.service.command.UpdateUserProfileCommand;
import com.ssafy.chaing.user.service.dto.UserDTO;
import com.ssafy.chaing.user.service.dto.UserDetailInfoDTO;
import com.ssafy.chaing.user.service.dto.UserProfileDTO;

public interface UserService {

//    List<UserDTO> getAllUsers();

    UserDTO getMe(Long userId);

    UserProfileDTO getMyProfile(Long userId);

    UserProfileDTO updateMyProfile(UpdateUserProfileCommand command);

    UserDetailInfoDTO getUserInfo(Long userId);
//
//    void deleteUser(Long userId);

}
