package com.kimhok.tickets.service;

import com.kimhok.tickets.common.enums.UserStatus;
import com.kimhok.tickets.dto.auth.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO updateProfile(UserDTO userDTO,String userId);
    void removeUser(String userId);
    List<UserDTO> getActiveUser();
    Map<String,Object> getCurrentUserProfile();
    UserDTO updateStatus(String userId, UserStatus status);
}
