package com.skillswap.backend.service;

import com.skillswap.backend.dto.request.UpdateProfileRequest;
import com.skillswap.backend.dto.request.UserSkillRequest;
import com.skillswap.backend.dto.response.UserProfileResponse;
import com.skillswap.backend.dto.response.UserSkillResponse;
import com.skillswap.backend.entity.Skill;
import com.skillswap.backend.entity.User;
import com.skillswap.backend.entity.UserSkill;
import com.skillswap.backend.exception.DuplicateResourceException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.SkillRepository;
import com.skillswap.backend.repository.UserRepository;
import com.skillswap.backend.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;

    public UserProfileResponse getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<UserSkillResponse> teaching = getSkillsForUser(userId, UserSkill.RelationType.CAN_TEACH);
        List<UserSkillResponse> learning = getSkillsForUser(userId, UserSkill.RelationType.WANTS_TO_LEARN);

        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .bio(user.getBio())
                .location(user.getLocation())
                .profileImageUrl(user.getProfileImageUrl())
                .experienceLevel(user.getExperienceLevel() != null ? user.getExperienceLevel().name() : null)
                .accountStatus(user.getAccountStatus() != null ? user.getAccountStatus().name() : null)
                .createdAt(user.getCreatedAt())
                .teachingSkills(teaching)
                .learningSkills(learning)
                .build();
    }

    public UserProfileResponse updateProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getLocation() != null) user.setLocation(request.getLocation());
        if (request.getProfileImageUrl() != null) user.setProfileImageUrl(request.getProfileImageUrl());
        if (request.getExperienceLevel() != null) {
            user.setExperienceLevel(User.ExperienceLevel.valueOf(request.getExperienceLevel().toUpperCase()));
        }
        user.setUpdatedAt(Instant.now());

        userRepository.save(user);
        return getProfile(userId);
    }

    public List<UserSkillResponse> getSkillsForUser(String userId, UserSkill.RelationType type) {
        return userSkillRepository.findByUserIdAndRelationType(userId, type).stream()
                .map(this::toUserSkillResponse)
                .toList();
    }

    public UserSkillResponse addUserSkill(String userId, UserSkillRequest request) {
        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        UserSkill.RelationType relationType =
                UserSkill.RelationType.valueOf(request.getRelationType().toUpperCase());

        boolean alreadyExists = userSkillRepository.existsByUserIdAndSkillIdAndRelationType(
                userId, skill.getId(), relationType);

        if (alreadyExists) {
            throw new DuplicateResourceException("This skill relationship already exists for the user");
        }

        UserSkill userSkill = UserSkill.builder()
                .userId(userId)
                .skillId(skill.getId())
                .relationType(relationType)
                .level(request.getLevel() != null
                        ? UserSkill.SkillLevel.valueOf(request.getLevel().toUpperCase())
                        : UserSkill.SkillLevel.BEGINNER)
                .createdAt(Instant.now())
                .build();

        userSkillRepository.save(userSkill);
        return toUserSkillResponse(userSkill);
    }

    public void removeUserSkill(String userId, String skillId) {
        List<UserSkill> matches = userSkillRepository.findByUserId(userId).stream()
                .filter(us -> us.getSkillId().equals(skillId))
                .toList();

        if (matches.isEmpty()) {
            throw new ResourceNotFoundException("Skill relationship not found for this user");
        }

        userSkillRepository.deleteAll(matches);
    }

    private UserSkillResponse toUserSkillResponse(UserSkill us) {
        Skill skill = skillRepository.findById(us.getSkillId()).orElse(null);
        return UserSkillResponse.builder()
                .id(us.getId())
                .skillId(us.getSkillId())
                .skillName(skill != null ? skill.getName() : "Unknown")
                .relationType(us.getRelationType().name())
                .level(us.getLevel() != null ? us.getLevel().name() : null)
                .build();
    }
}