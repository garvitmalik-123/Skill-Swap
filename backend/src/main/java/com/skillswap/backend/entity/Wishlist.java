package com.skillswap.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wishlists")
@CompoundIndex(name = "user_course_unique", def = "{'userId': 1, 'courseId': 1}", unique = true)
public class Wishlist {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String courseId;

    @CreatedDate
    private Instant addedAt;
}