package com.skillswap.backend.controller;

import com.skillswap.backend.dto.response.ApiResponse;
import com.skillswap.backend.dto.response.CertificateResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> getMyCertificates(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<CertificateResponse> certificates = certificateService.getCertificatesForUser(currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success(certificates));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CertificateResponse>> getCertificateById(@PathVariable String id) {
        CertificateResponse response = certificateService.getCertificateById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/verify/{certificateCode}")
    public ResponseEntity<ApiResponse<CertificateResponse>> verifyCertificate(@PathVariable String certificateCode) {
        CertificateResponse response = certificateService.verifyCertificate(certificateCode);
        return ResponseEntity.ok(ApiResponse.success("Certificate is valid", response));
    }
}