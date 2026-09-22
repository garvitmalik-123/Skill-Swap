package com.skillswap.backend.repository;

import com.skillswap.backend.dto.request.CourseSearchRequest;
import com.skillswap.backend.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Course> search(CourseSearchRequest filters, Pageable pageable) {
        List<Criteria> criteriaList = new ArrayList<>();

        // Only published courses are discoverable
        criteriaList.add(Criteria.where("status").is(Course.CourseStatus.PUBLISHED));

        if (filters.getKeyword() != null && !filters.getKeyword().isBlank()) {
            Pattern pattern = Pattern.compile(Pattern.quote(filters.getKeyword()), Pattern.CASE_INSENSITIVE);
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("title").regex(pattern),
                    Criteria.where("description").regex(pattern)
            ));
        }

        if (filters.getCategory() != null && !filters.getCategory().isBlank()) {
            criteriaList.add(Criteria.where("category").is(filters.getCategory()));
        }

        if (filters.getSkill() != null && !filters.getSkill().isBlank()) {
            criteriaList.add(Criteria.where("skills").in(filters.getSkill()));
        }

        if (filters.getCreatorId() != null && !filters.getCreatorId().isBlank()) {
            criteriaList.add(Criteria.where("creatorId").is(filters.getCreatorId()));
        }

        if (filters.getType() != null) {
            criteriaList.add(Criteria.where("type").is(filters.getType()));
        }

        if (filters.getDifficulty() != null) {
            criteriaList.add(Criteria.where("difficulty").is(filters.getDifficulty()));
        }

        if (filters.getLanguage() != null && !filters.getLanguage().isBlank()) {
            criteriaList.add(Criteria.where("language").is(filters.getLanguage()));
        }

        if (filters.getMinPrice() != null || filters.getMaxPrice() != null) {
            Criteria priceCriteria = Criteria.where("price");
            if (filters.getMinPrice() != null) priceCriteria = priceCriteria.gte(filters.getMinPrice());
            if (filters.getMaxPrice() != null) priceCriteria = priceCriteria.lte(filters.getMaxPrice());
            criteriaList.add(priceCriteria);
        }

        if (filters.getMinRating() != null) {
            criteriaList.add(Criteria.where("averageRating").gte(filters.getMinRating()));
        }

        if (filters.getMinDuration() != null || filters.getMaxDuration() != null) {
            Criteria durationCriteria = Criteria.where("durationMinutes");
            if (filters.getMinDuration() != null) durationCriteria = durationCriteria.gte(filters.getMinDuration());
            if (filters.getMaxDuration() != null) durationCriteria = durationCriteria.lte(filters.getMaxDuration());
            criteriaList.add(durationCriteria);
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Course.class);

        query.with(pageable);
        List<Course> results = mongoTemplate.find(query, Course.class);

        return new PageImpl<>(results, pageable, total);
    }
}