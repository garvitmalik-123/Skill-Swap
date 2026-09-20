package com.skillswap.backend.controller;

import com.skillswap.backend.dto.request.UpdateProfileRequest;
import com.skillswap.backend.dto.request.UserSkillRequest;
import com.skillswap.backend.dto.response.UserProfileResponse;
import com.skillswap.backend.dto.response.UserSkillResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userProfileService.getProfile(id));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userProfileService.updateProfile(currentUser.getUserId(), request));
    }

    @GetMapping("/me/skills")
    public ResponseEntity<UserProfileResponse> getMyProfileWithSkills(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(userProfileService.getProfile(currentUser.getUserId()));
    }

    @PostMapping("/me/skills")
    public ResponseEntity<UserSkillResponse> addSkill(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody UserSkillRequest request) {
        return ResponseEntity.ok(userProfileService.addUserSkill(currentUser.getUserId(), request));
    }

    @DeleteMapping("/me/skills/{skillId}")
    public ResponseEntity<Void> removeSkill(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String skillId) {
        userProfileService.removeUserSkill(currentUser.getUserId(), skillId);
        return ResponseEntity.noContent().build();
    }
}