package com.kimhok.tickets.service.impl;

import com.kimhok.tickets.common.enums.UserStatus;
import com.kimhok.tickets.dto.UserDTO;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.exception.ResourceNotFoundException;
import com.kimhok.tickets.mapper.UserMapper;
import com.kimhok.tickets.repository.UserRepository;
import com.kimhok.tickets.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.info("User Service Get All User..." );
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO updateProfile(UserDTO userDTO, String userId) {
        log.info("User Service Update User '{}'...", userDTO.getEmail());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        userMapper.updateUserFromDto(userDTO, user);

        if (userDTO.getStatus() != null) {
            UserStatus newStatus = UserStatus.valueOf(userDTO.getStatus().toString());
            user.setStatus(newStatus);
        }

        return userMapper.toDto(userRepository.save(user));
    }


    @Override
    @Transactional
    public void removeUser(String userId) {
        log.info("User Service remove User '{}'...",userId);
        User isExisting = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        if (isExisting != null){
            userRepository.deleteById(userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getActiveUser() {
        log.info("User Service Get All Active User..." );
        return userRepository.findByStatus(UserStatus.ACTIVE)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
