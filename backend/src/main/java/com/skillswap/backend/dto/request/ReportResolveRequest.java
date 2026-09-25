package com.skillswap.backend.dto.request;

import com.skillswap.backend.entity.Report.ReportStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReportResolveRequest {

    @NotNull
    private ReportStatus status; // RESOLVED or DISMISSED

    @Size(max = 1000)
    private String resolutionNote;
}