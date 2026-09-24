package com.skillswap.backend.controller;

import com.skillswap.backend.dto.response.ApiResponse;
import com.skillswap.backend.dto.response.WishlistItemResponse;
import com.skillswap.backend.security.CustomUserDetails;
import com.skillswap.backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{courseId}")
    public ResponseEntity<ApiResponse<WishlistItemResponse>> addToWishlist(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String courseId) {
        WishlistItemResponse response = wishlistService.addToWishlist(currentUser.getUserId(), courseId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Added to wishlist", response));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable String courseId) {
        wishlistService.removeFromWishlist(currentUser.getUserId(), courseId);
        return ResponseEntity.ok(ApiResponse.success("Removed from wishlist", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WishlistItemResponse>>> getWishlist(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<WishlistItemResponse> wishlist = wishlistService.getWishlist(currentUser.getUserId());
        return ResponseEntity.ok(ApiResponse.success(wishlist));
    }
}