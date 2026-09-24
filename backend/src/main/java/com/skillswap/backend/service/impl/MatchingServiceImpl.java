package com.skillswap.backend.service.impl;

import com.skillswap.backend.dto.response.MatchResponse;
import com.skillswap.backend.entity.Skill;
import com.skillswap.backend.entity.User;
import com.skillswap.backend.entity.UserSkill;
import com.skillswap.backend.entity.UserSkill.RelationType;
import com.skillswap.backend.exception.ResourceNotFoundException;
import com.skillswap.backend.repository.SkillRepository;
import com.skillswap.backend.repository.UserRepository;
import com.skillswap.backend.repository.UserSkillRepository;
import com.skillswap.backend.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;

    @Override
    public List<MatchResponse> findMatches(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // My skillIds, split by relation type
        Set<String> myTeachSkillIds = toSkillIdSet(userId, RelationType.CAN_TEACH);
        Set<String> myLearnSkillIds = toSkillIdSet(userId, RelationType.WANTS_TO_LEARN);

        if (myTeachSkillIds.isEmpty() && myLearnSkillIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<User> candidates = userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(userId))
                .toList();

        // Pre-resolve skill names once (avoid N+1 lookups)
        Map<String, String> skillNamesById = skillRepository.findAll().stream()
                .collect(Collectors.toMap(Skill::getId, Skill::getName));

        List<MatchResponse> matches = new ArrayList<>();

        for (User other : candidates) {
            Set<String> theirTeachSkillIds = toSkillIdSet(other.getId(), RelationType.CAN_TEACH);
            Set<String> theirLearnSkillIds = toSkillIdSet(other.getId(), RelationType.WANTS_TO_LEARN);

            // They teach what I want to learn
            List<String> theyCanTeachMeIds = theirTeachSkillIds.stream()
                    .filter(myLearnSkillIds::contains)
                    .toList();

            // They want to learn what I teach
            List<String> theyWantFromMeIds = theirLearnSkillIds.stream()
                    .filter(myTeachSkillIds::contains)
                    .toList();

            if (!theyCanTeachMeIds.isEmpty() && !theyWantFromMeIds.isEmpty()) {
                double score = computeScore(
                        theyCanTeachMeIds.size(), theyWantFromMeIds.size(),
                        myLearnSkillIds.size(), myTeachSkillIds.size());

                matches.add(MatchResponse.builder()
                        .userId(other.getId())
                        .name(other.getName())
                        .bio(other.getBio())
                        .theyCanTeach(resolveNames(theyCanTeachMeIds, skillNamesById))
                        .theyWantToLearn(resolveNames(theyWantFromMeIds, skillNamesById))
                        .compatibilityScore(score)
                        .build());
            }
        }

        matches.sort(Comparator.comparingDouble(MatchResponse::getCompatibilityScore).reversed());
        return matches;
    }

    private Set<String> toSkillIdSet(String userId, RelationType relationType) {
        return userSkillRepository.findByUserIdAndRelationType(userId, relationType)
                .stream()
                .map(UserSkill::getSkillId)
                .collect(Collectors.toSet());
    }

    private List<String> resolveNames(List<String> skillIds, Map<String, String> skillNamesById) {
        return skillIds.stream()
                .map(id -> skillNamesById.getOrDefault(id, id)) // fallback to raw id if name missing
                .toList();
    }

    private double computeScore(int teachOverlap, int learnOverlap, int myLearnTotal, int myTeachTotal) {
        double teachRatio = myLearnTotal == 0 ? 0 : (double) teachOverlap / myLearnTotal;
        double learnRatio = myTeachTotal == 0 ? 0 : (double) learnOverlap / myTeachTotal;
        return (teachRatio + learnRatio) / 2.0;
    }
}