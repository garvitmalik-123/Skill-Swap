package com.skillswap.backend.service;

import com.skillswap.backend.dto.response.WishlistItemResponse;

import java.util.List;

public interface WishlistService {

    WishlistItemResponse addToWishlist(String userId, String courseId);

    void removeFromWishlist(String userId, String courseId);

    List<WishlistItemResponse> getWishlist(String userId);
}