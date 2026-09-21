package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.response.CertificateResponse;
import com.skillswap.backend.entity.Certificate;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.CertificateRepository;
import com.skillswap.backend.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;

    @Override
    public CertificateResponse generateCertificate(String userId, String userName, String courseId, String courseTitle) {
        if (certificateRepository.existsByUserIdAndCourseId(userId, courseId)) {
            Certificate existing = certificateRepository.findByUserIdAndCourseId(userId, courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
            return toResponse(existing);
        }

        String code = "SKW-" + Year.now().getValue() + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Certificate certificate = Certificate.builder()
                .certificateCode(code)
                .userId(userId)
                .userName(userName)
                .courseId(courseId)
                .courseTitle(courseTitle)
                .completionDate(Instant.now())
                .verified(true)
                .build();

        Certificate saved = certificateRepository.save(certificate);
        return toResponse(saved);
    }

    @Override
    public CertificateResponse getCertificateById(String certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with id: " + certificateId));
        return toResponse(certificate);
    }

    @Override
    public CertificateResponse verifyCertificate(String certificateCode) {
        Certificate certificate = certificateRepository.findByCertificateCode(certificateCode)
                .orElseThrow(() -> new BadRequestException("Invalid certificate code"));
        return toResponse(certificate);
    }

    @Override
    public List<CertificateResponse> getCertificatesForUser(String userId) {
        return certificateRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CertificateResponse toResponse(Certificate certificate) {
        return CertificateResponse.builder()
                .id(certificate.getId())
                .certificateCode(certificate.getCertificateCode())
                .userId(certificate.getUserId())
                .userName(certificate.getUserName())
                .courseId(certificate.getCourseId())
                .courseTitle(certificate.getCourseTitle())
                .completionDate(certificate.getCompletionDate())
                .issuedAt(certificate.getIssuedAt())
                .verified(certificate.isVerified())
                .build();
    }
}