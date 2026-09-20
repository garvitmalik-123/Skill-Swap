package com.skillswap.backend.repository;

import com.skillswap.backend.entity.UserSkill;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserSkillRepository extends MongoRepository<UserSkill, String> {

    List<UserSkill> findByUserId(String userId);

    List<UserSkill> findByUserIdAndRelationType(String userId, UserSkill.RelationType relationType);

    Optional<UserSkill> findByUserIdAndSkillIdAndRelationType(
            String userId, String skillId, UserSkill.RelationType relationType);

    boolean existsByUserIdAndSkillIdAndRelationType(
            String userId, String skillId, UserSkill.RelationType relationType);

    List<UserSkill> findBySkillIdAndRelationType(String skillId, UserSkill.RelationType relationType);
}