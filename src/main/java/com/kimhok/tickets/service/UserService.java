package com.kimhok.tickets.service;

import com.kimhok.tickets.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO updateProfile(UserDTO userDTO,String userId);
    void removeUser(String userId);
    List<UserDTO> getActiveUser();
}
