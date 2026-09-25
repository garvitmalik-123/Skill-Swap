package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.response.CourseResponse;
import com.skillswap.backend.dto.response.WishlistItemResponse;
import com.skillswap.backend.entity.Course;
import com.skillswap.backend.entity.Wishlist;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.CourseRepository;
import com.skillswap.backend.repository.WishlistRepository;
import com.skillswap.backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final CourseRepository courseRepository;

    @Override
    public WishlistItemResponse addToWishlist(String userId, String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (wishlistRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new BadRequestException("Course is already in your wishlist");
        }

        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .courseId(courseId)
                .build();

        try {
            Wishlist saved = wishlistRepository.save(wishlist);
            return toResponse(saved, course);
        } catch (DuplicateKeyException ex) {
            // Safety net in case of a race condition past the existsBy check above.
            throw new BadRequestException("Course is already in your wishlist");
        }
    }

    @Override
    public void removeFromWishlist(String userId, String courseId) {
        if (!wishlistRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new ResourceNotFoundException("Course is not in your wishlist");
        }
        wishlistRepository.deleteByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public List<WishlistItemResponse> getWishlist(String userId) {
        List<Wishlist> items = wishlistRepository.findByUserId(userId);

        return items.stream()
                .map(item -> {
                    Course course = courseRepository.findById(item.getCourseId()).orElse(null);
                    return toResponse(item, course);
                })
                .collect(Collectors.toList());
    }

    private WishlistItemResponse toResponse(Wishlist wishlist, Course course) {
        CourseResponse courseResponse = null;
        if (course != null) {
            courseResponse = CourseResponse.builder()
                    .id(course.getId())
                    .creatorId(course.getCreatorId())
                    .title(course.getTitle())
                    .description(course.getDescription())
                    .category(course.getCategory())
                    .skills(course.getSkills())
                    .difficulty(course.getDifficulty())
                    .language(course.getLanguage())
                    .durationMinutes(course.getDurationMinutes())
                    .thumbnailUrl(course.getThumbnailUrl())
                    .type(course.getType())
                    .price(course.getPrice())
                    .skillPointCost(course.getSkillPointCost())
                    .status(course.getStatus())
                    .averageRating(course.getAverageRating())
                    .reviewCount(course.getReviewCount())
                    .enrollmentCount(course.getEnrollmentCount())
                    .build();
        }

        return WishlistItemResponse.builder()
                .id(wishlist.getId())
                .courseId(wishlist.getCourseId())
                .course(courseResponse)
                .addedAt(wishlist.getAddedAt())
                .build();
    }
}