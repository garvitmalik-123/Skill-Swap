package com.skillswap.skillswap.backend.service;

import com.skillswap.skillswap.backend.dto.request.ForgotPasswordRequest;
import com.skillswap.skillswap.backend.dto.request.LoginRequest;
import com.skillswap.skillswap.backend.dto.request.RegisterRequest;
import com.skillswap.skillswap.backend.dto.request.ResetPasswordRequest;
import com.skillswap.skillswap.backend.dto.response.AuthResponse;
import com.skillswap.skillswap.backend.dto.response.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    UserResponse getCurrentUser(String userId);
}