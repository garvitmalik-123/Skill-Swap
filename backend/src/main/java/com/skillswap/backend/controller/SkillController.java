package com.skillswap.backend.controller;

import com.skillswap.backend.dto.response.SkillResponse;
import com.skillswap.backend.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
@Tag(name = "Skills", description = "Skill catalog and user skill management")

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAllSkills(
            @RequestParam(required = false) String categoryId) {
        if (categoryId != null) {
            return ResponseEntity.ok(skillService.getSkillsByCategory(categoryId));
        }
        return ResponseEntity.ok(skillService.getAllSkills());
    }
}