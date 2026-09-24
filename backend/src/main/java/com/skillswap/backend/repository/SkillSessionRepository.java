package com.skillswap.backend.repository;

import com.skillswap.backend.entity.SkillSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SkillSessionRepository extends MongoRepository<SkillSession, String> {

    Page<SkillSession> findByStatus(SkillSession.SessionStatus status, Pageable pageable);

    Page<SkillSession> findBySkillIdAndStatus(String skillId, SkillSession.SessionStatus status, Pageable pageable);

    Page<SkillSession> findByTeacherId(String teacherId, Pageable pageable);
}