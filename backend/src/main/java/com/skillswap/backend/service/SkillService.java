package com.skillswap.backend.service;

import com.skillswap.backend.dto.response.SkillResponse;
import com.skillswap.backend.entity.Skill;
import com.skillswap.backend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SkillResponse> getSkillsByCategory(String categoryId) {
        return skillRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponse)
                .toList();
    }

    private SkillResponse toResponse(Skill s) {
        return SkillResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .categoryId(s.getCategoryId())
                .description(s.getDescription())
                .build();
    }
}