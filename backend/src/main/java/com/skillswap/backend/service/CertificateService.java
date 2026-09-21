package com.skillswap.backend.service;

import com.skillswap.backend.dto.response.CertificateResponse;

import java.util.List;

public interface CertificateService {

    /** Called by the enrollment/progress flow once a user completes a course. */
    CertificateResponse generateCertificate(String userId, String userName, String courseId, String courseTitle);

    CertificateResponse getCertificateById(String certificateId);

    CertificateResponse verifyCertificate(String certificateCode);

    List<CertificateResponse> getCertificatesForUser(String userId);
}