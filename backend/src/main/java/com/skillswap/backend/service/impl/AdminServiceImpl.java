package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.request.CourseRejectionRequest;
import com.skillswap.backend.dto.response.*;
import com.skillswap.backend.entity.*;
import com.skillswap.backend.entity.Notification.NotificationType;
import com.skillswap.backend.entity.User.AccountStatus;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.*;
import com.skillswap.backend.service.AdminService;
import com.skillswap.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;
    private final SkillExchangeRequestRepository exchangeRepository;
    private final SkillSessionRepository sessionRepository;
    private final SessionBookingRepository bookingRepository;
    private final SkillPointTransactionRepository skillPointTransactionRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final NotificationService notificationService;

    @Override
    public Page<AdminUserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toAdminUserResponse);
    }

    @Override
    public void suspendUser(String userId, String adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRoles() != null && user.getRoles().contains(User.Role.ADMIN)) {
            throw new BadRequestException("Cannot suspend an admin account");
        }

        if (user.getAccountStatus() == AccountStatus.SUSPENDED) {
            throw new BadRequestException("User is already suspended");
        }

        user.setAccountStatus(AccountStatus.SUSPENDED);
        userRepository.save(user);

        notificationService.notify(
                userId,
                NotificationType.ADMIN_ACTION,
                "Account suspended",
                "Your account has been suspended by an administrator",
                userId,
                "USER");
    }

    @Override
    public void reactivateUser(String userId, String adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getAccountStatus() != AccountStatus.SUSPENDED) {
            throw new BadRequestException("User is not currently suspended");
        }

        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);

        notificationService.notify(
                userId,
                NotificationType.ADMIN_ACTION,
                "Account reactivated",
                "Your account has been reactivated",
                userId,
                "USER");
    }

    @Override
    public Page<AdminTransactionResponse> getAllTransactions(Pageable pageable) {
        List<AdminTransactionResponse> combined = new ArrayList<>();

        skillPointTransactionRepository.findAll().forEach(t -> combined.add(
                AdminTransactionResponse.builder()
                        .id(t.getId())
                        .source("SKILLPOINT")
                        .userId(t.getUserId())
                        .type(t.getType().name())
                        .amount(t.getAmount())
                        .description(t.getDescription())
                        .createdAt(t.getCreatedAt())
                        .build()));

        walletTransactionRepository.findAll().forEach(t -> combined.add(
                AdminTransactionResponse.builder()
                        .id(t.getId())
                        .source("WALLET")
                        .userId(t.getCreatorId())
                        .type(t.getType().name())
                        .amount(t.getAmount())
                        .description(t.getDescription())
                        .createdAt(t.getCreatedAt())
                        .build()));

        combined.sort(Comparator.comparing(AdminTransactionResponse::getCreatedAt).reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), combined.size());
        List<AdminTransactionResponse> pageContent = start > combined.size()
                ? List.of()
                : combined.subList(start, end);

        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, combined.size());
    }

    @Override
    public AnalyticsResponse getAnalytics() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.findAll().stream()
                .filter(u -> u.getAccountStatus() == AccountStatus.ACTIVE)
                .count();
        long suspendedUsers = userRepository.findAll().stream()
                .filter(u -> u.getAccountStatus() == AccountStatus.SUSPENDED)
                .count();

        long totalCourses = courseRepository.count();
        long publishedCourses = courseRepository.findAll().stream()
                .filter(c -> c.getStatus() == Course.CourseStatus.PUBLISHED)
                .count();

        double avgRating = courseRepository.findAll().stream()
                .mapToDouble(Course::getAverageRating)
                .filter(r -> r > 0)
                .average()
                .orElse(0.0);

        long completedExchanges = exchangeRepository.findAll().stream()
                .filter(e -> e.getStatus() == SkillExchangeRequest.ExchangeStatus.COMPLETED)
                .count();

        return AnalyticsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .suspendedUsers(suspendedUsers)
                .totalCourses(totalCourses)
                .publishedCourses(publishedCourses)
                .totalEnrollments(enrollmentRepository.count())
                .totalReviews(reviewRepository.count())
                .averagePlatformRating(Math.round(avgRating * 100.0) / 100.0)
                .totalSkillExchangeRequests(exchangeRepository.count())
                .completedSkillExchanges(completedExchanges)
                .totalSkillSessions(sessionRepository.count())
                .totalBookings(bookingRepository.count())
                .build();
    }

    @Override
    public Page<AdminCourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(this::toAdminCourseResponse);
    }

    @Override
    public void approveCourse(String courseId, String adminId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        course.setStatus(Course.CourseStatus.PUBLISHED);
        course.setRejectionReason(null);
        courseRepository.save(course);

        notificationService.notify(
                course.getCreatorId(),
                NotificationType.ADMIN_ACTION,
                "Course approved",
                "Your course \"" + course.getTitle() + "\" has been approved and is now live",
                course.getId(),
                "COURSE");
    }

    @Override
    public void rejectCourse(String courseId, String adminId, CourseRejectionRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        course.setStatus(Course.CourseStatus.ARCHIVED);
        course.setRejectionReason(request.getReason());
        courseRepository.save(course);

        notificationService.notify(
                course.getCreatorId(),
                NotificationType.ADMIN_ACTION,
                "Course rejected",
                "Your course \"" + course.getTitle() + "\" was rejected: " + request.getReason(),
                course.getId(),
                "COURSE");
    }

    private AdminUserResponse toAdminUserResponse(User user) {
        java.util.Set<String> roleNames = user.getRoles() == null
                ? java.util.Set.of()
                : user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet());

        return AdminUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roleNames)
                .accountStatus(user.getAccountStatus())
                .experienceLevel(user.getExperienceLevel())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private AdminCourseResponse toAdminCourseResponse(Course course) {
        String creatorName = userRepository.findById(course.getCreatorId())
                .map(User::getName).orElse("Unknown");

        return AdminCourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .creatorId(course.getCreatorId())
                .creatorName(creatorName)
                .type(course.getType())
                .status(course.getStatus())
                .rejectionReason(course.getRejectionReason())
                .averageRating(course.getAverageRating())
                .enrollmentCount(course.getEnrollmentCount())
                .createdAt(course.getCreatedAt())
                .build();
    }
}