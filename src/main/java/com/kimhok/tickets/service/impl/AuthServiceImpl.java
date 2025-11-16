package com.kimhok.tickets.service.impl;

import com.kimhok.tickets.common.enums.UserStatus;
import com.kimhok.tickets.config.security.CustomUserDetails;
import com.kimhok.tickets.config.security.JwtService;
import com.kimhok.tickets.config.utils.EmailService;
import com.kimhok.tickets.dto.auth.*;
import com.kimhok.tickets.entity.RefreshToken;
import com.kimhok.tickets.entity.Role;
import com.kimhok.tickets.entity.User;
import com.kimhok.tickets.entity.UserRole;
import com.kimhok.tickets.exception.ConflictException;
import com.kimhok.tickets.exception.ResourceNotFoundException;
import com.kimhok.tickets.mapper.UserMapper;
import com.kimhok.tickets.repository.RefreshTokenRepository;
import com.kimhok.tickets.repository.RoleRepository;
import com.kimhok.tickets.repository.UserRepository;
import com.kimhok.tickets.repository.UserRoleRepository;
import com.kimhok.tickets.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private final EmailService emailService;
    private static final String DEFAULT_ROLE = "USER";
    @Value("${frontend.uri}")
    private String frontendUrl;

    @Transactional
    @Override
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        log.info("Auth Service Register Started..." + request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("This Email already registered " + request.getEmail());
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadCredentialsException("Password and Confirm Password do not match");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setStatus(UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);
        log.info("Created new user with ID: {}", savedUser.getId());
        assignDefaultRole(savedUser);
        return RegisterResponseDTO.builder()
                .message("Register Successfully")
                .email(request.getEmail())
                .build();
    }

    private void assignDefaultRole(User user) {
        Optional<Role> defaultRole = roleRepository.findRoleByName(DEFAULT_ROLE);

        if (defaultRole.isEmpty()) {
            log.error("Default role '{}' not found in database", DEFAULT_ROLE);
            throw new IllegalStateException("Default role not found. Please ensure '" + DEFAULT_ROLE + "' role exists in the database.");
        }

        UserRole userRole = new UserRole(user, defaultRole.get(), user); // Self-assigned
        user.setUserRole(userRole);
        userRoleRepository.save(userRole);

        log.info("Assigned default role '{}' to user with ID: {}", DEFAULT_ROLE, user.getId());
    }


    @Transactional
    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Auth Service Login Started..." + request.getEmail());

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var userDetails = (CustomUserDetails) authentication.getPrincipal();
        var jwtToken = jwtService.generateToken(userDetails);

        Optional<RefreshToken> isExistingRefresh = refreshTokenRepository.findByUser(userDetails.getUser());
        String refreshToken;

        if (isExistingRefresh.isPresent() && isExistingRefresh.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            refreshToken = isExistingRefresh.get().getToken();
        } else {
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);
            RefreshToken refreshTokenEntity = isExistingRefresh.orElseGet(() ->
                    RefreshToken.builder()
                            .user(userDetails.getUser())
                            .build()
            );
            refreshTokenEntity.setToken(newRefreshToken);
            refreshTokenEntity.setExpiryDate(LocalDateTime.now().plusDays(7));
            refreshTokenRepository.save(refreshTokenEntity);
            refreshToken = newRefreshToken;
        }

        var userResponse = userMapper.toDto(userDetails.getUser());
        return LoginResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }

    @Override
    @Transactional
    public RefreshTokenResponseDTO refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));

        if (jwtService.isTokenExpired(refreshToken)) {
            throw new ResourceNotFoundException("Refresh token has expired. Please login again.");
        }

        var userDetails = new CustomUserDetails(token.getUser());
        String newAccessToken = jwtService.generateToken(userDetails);

        return RefreshTokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequestDTO request) {
        log.info("Auth Service Change Password");
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        log.info("===== forgot password requested by {} =====", request.getEmail());
        Optional<User> isExisting = Optional.ofNullable(userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("This email not found please use correct email " + request.getEmail())));

        User user = isExisting.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), resetUrl);
        log.info("===== Email is sending to {} ===== ", user.getEmail());
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        log.info("===== reset password processing =====");
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(()->new ResourceNotFoundException("this token not found!"));
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())){
            throw new ResourceNotFoundException("This token was expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        log.info("===== user was reset password success =====");
    }
}
