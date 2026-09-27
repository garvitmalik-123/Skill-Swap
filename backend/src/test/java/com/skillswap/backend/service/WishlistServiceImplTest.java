package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.response.WishlistItemResponse;
import com.skillswap.backend.entity.Course;
import com.skillswap.backend.exception.BadRequestException;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.CourseRepository;
import com.skillswap.backend.repository.WishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    @Test
    void addToWishlist_shouldThrowResourceNotFound_whenCourseDoesNotExist() {
        when(courseRepository.findById("missing-course")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wishlistService.addToWishlist("user-1", "missing-course"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addToWishlist_shouldThrowBadRequest_whenAlreadyInWishlist() {
        Course course = Course.builder().id("course-1").build();

        when(courseRepository.findById("course-1")).thenReturn(Optional.of(course));
        when(wishlistRepository.existsByUserIdAndCourseId("user-1", "course-1")).thenReturn(true);

        assertThatThrownBy(() -> wishlistService.addToWishlist("user-1", "course-1"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already in your wishlist");
    }

    @Test
    void removeFromWishlist_shouldThrowResourceNotFound_whenNotInWishlist() {
        when(wishlistRepository.existsByUserIdAndCourseId("user-1", "course-1")).thenReturn(false);

        assertThatThrownBy(() -> wishlistService.removeFromWishlist("user-1", "course-1"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}