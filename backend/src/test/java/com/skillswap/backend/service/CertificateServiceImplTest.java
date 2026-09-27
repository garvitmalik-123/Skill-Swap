package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.response.CertificateResponse;
import com.skillswap.backend.entity.Certificate;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.repository.CertificateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    @Test
    void generateCertificate_shouldReturnExisting_whenAlreadyIssuedForUserAndCourse() {
        Certificate existing = Certificate.builder()
                .id("cert-1")
                .certificateCode("SKW-2026-ABC12345")
                .userId("user-1")
                .courseId("course-1")
                .userName("Test User")
                .courseTitle("Java Basics")
                .completionDate(Instant.now())
                .verified(true)
                .build();

        when(certificateRepository.existsByUserIdAndCourseId("user-1", "course-1")).thenReturn(true);
        when(certificateRepository.findByUserIdAndCourseId("user-1", "course-1"))
                .thenReturn(Optional.of(existing));

        CertificateResponse response = certificateService.generateCertificate(
                "user-1", "Test User", "course-1", "Java Basics");

        assertThat(response.getCertificateCode()).isEqualTo("SKW-2026-ABC12345");
    }

    @Test
    void generateCertificate_shouldCreateNew_whenNoneExistsYet() {
        when(certificateRepository.existsByUserIdAndCourseId("user-1", "course-1")).thenReturn(false);
        when(certificateRepository.save(any(Certificate.class))).thenAnswer(invocation -> {
            Certificate c = invocation.getArgument(0);
            c.setId("cert-new");
            return c;
        });

        CertificateResponse response = certificateService.generateCertificate(
                "user-1", "Test User", "course-1", "Java Basics");

        assertThat(response.getCertificateCode()).startsWith("SKW-");
        assertThat(response.isVerified()).isTrue();
    }

    @Test
    void verifyCertificate_shouldThrowBadRequest_whenCodeIsInvalid() {
        when(certificateRepository.findByCertificateCode("INVALID-CODE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> certificateService.verifyCertificate("INVALID-CODE"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid certificate code");
    }
}