package com.skillswap.backend.repository;

import com.skillswap.backend.entity.Certificate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends MongoRepository<Certificate, String> {

    Optional<Certificate> findByCertificateCode(String certificateCode);

    List<Certificate> findByUserId(String userId);

    boolean existsByUserIdAndCourseId(String userId, String courseId);

    Optional<Certificate> findByUserIdAndCourseId(String userId, String courseId);
}