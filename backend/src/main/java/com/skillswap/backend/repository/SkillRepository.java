package com.skillswap.backend.repository;

import com.skillswap.backend.entity.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface SkillRepository extends MongoRepository<Skill, String> {
    Optional<Skill> findByName(String name);
    boolean existsByName(String name);
    List<Skill> findByCategoryId(String categoryId);
}