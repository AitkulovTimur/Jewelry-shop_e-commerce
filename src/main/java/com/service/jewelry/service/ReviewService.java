package com.service.jewelry.service;

import com.service.jewelry.model.dto.ReviewDto;
import com.service.jewelry.model.entity.ReviewEntity;
import com.service.jewelry.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    public Set<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream().map(reviewEntity -> new ReviewDto(
                reviewEntity.getId(),
                reviewEntity.getName(),
                reviewEntity.getText()
        )).sorted(Comparator.comparing(ReviewDto::id)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void createReview(ReviewEntity review) {
        reviewRepository.save(review);
    }

}