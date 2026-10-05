package com.skillswap.backend.config;

import com.skillswap.backend.entity.Category;
import com.skillswap.backend.entity.Skill;
import com.skillswap.backend.repository.CategoryRepository;
import com.skillswap.backend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;

    @Override
    public void run(String... args) {
        Map<String, List<String>> data = new LinkedHashMap<>();
        data.put("Programming", List.of("Java", "Python", "JavaScript", "React", "Spring Boot", "SQL", "C++"));
        data.put("Design", List.of("UI/UX Design", "Graphic Design", "Figma", "Video Editing"));
        data.put("Business", List.of("Marketing", "Public Speaking", "Project Management", "Excel"));
        data.put("Languages", List.of("English", "Hindi", "Spanish", "French"));
        data.put("Music & Arts", List.of("Guitar", "Piano", "Drawing", "Photography"));
        data.put("Data & AI", List.of("Data Analysis", "Machine Learning"));

        data.forEach((categoryName, skillNames) -> {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> categoryRepository.save(
                            Category.builder().name(categoryName).build()));

            for (String skillName : skillNames) {
                if (!skillRepository.existsByName(skillName)) {
                    skillRepository.save(Skill.builder()
                            .name(skillName)
                            .categoryId(category.getId())
                            .build());
                }
            }
        });
    }
}