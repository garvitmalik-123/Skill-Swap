package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.ForgotPasswordRequest;
import com.skillswap.backend.dto.request.LoginRequest;
import com.skillswap.backend.dto.request.RegisterRequest;
import com.skillswap.backend.dto.request.ResetPasswordRequest;
import com.skillswap.backend.dto.response.AuthResponse;
import com.skillswap.backend.dto.response.UserResponse;
import com.skillswap.backend.entity.PasswordResetToken;
import com.skillswap.backend.entity.User;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.exception.UnauthorizedException;
import com.skillswap.backend.repository.PasswordResetTokenRepository;
import com.skillswap.backend.repository.UserRepository;
import com.skillswap.backend.security.JwtUtil;
import com.skillswap.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private static final long RESET_TOKEN_VALIDITY_MINUTES = 15;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with this email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(User.Role.USER))
                .accountStatus(User.AccountStatus.ACTIVE)
                .emailVerified(false)
                .createdAt(Instant.now())
                .build();

        User saved = userRepository.save(user);

        String token = jwtUtil.generateAccessToken(saved.getId(), saved.getEmail());

        return AuthResponse.builder()
                .accessToken(token)
                .userId(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase(), request.getPassword())
            );
        } catch (Exception ex) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        String token = jwtUtil.generateAccessToken(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .accessToken(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElse(null);

        if (user == null) {
            return;
        }

        resetTokenRepository.deleteByUserId(user.getId());

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .userId(user.getId())
                .expiresAt(Instant.now().plusSeconds(RESET_TOKEN_VALIDITY_MINUTES * 60))
                .used(false)
                .createdAt(Instant.now())
                .build();

        resetTokenRepository.save(resetToken);

        // TODO: send resetToken.getToken() to user's email via mail service
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        if (resetToken.isUsed() || resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("Invalid or expired reset token");
        }

        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
    }

    @Override
    public UserResponse getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .location(user.getLocation())
                .experienceLevel(user.getExperienceLevel() != null ? user.getExperienceLevel().name() : null)
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }
}