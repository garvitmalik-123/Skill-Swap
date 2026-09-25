package com.skillswap.backend.dto.response;

import lombok.*;

@Data
@Builder
public class AnalyticsResponse {
    private long totalUsers;
    private long activeUsers;
    private long suspendedUsers;
    private long totalCourses;
    private long publishedCourses;
    private long totalEnrollments;
    private long totalReviews;
    private double averagePlatformRating;
    private long totalSkillExchangeRequests;
    private long completedSkillExchanges;
    private long totalSkillSessions;
    private long totalBookings;
}