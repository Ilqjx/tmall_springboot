package com.ilqjx.service.impl;

import java.util.List;

import com.ilqjx.dao.ReviewRepository;
import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.Review;
import com.ilqjx.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    @CacheEvict(allEntries = true)
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    @Cacheable(key = "'reviews-count-pid-' + #p0.id")
    public int countByProduct(Product product) {
        return reviewRepository.countByProduct(product);
    }

    @Override
    @Cacheable(key = "'reviews-pid-' + #p0.id")
    public List<Review> listReviewByProduct(Product product) {
        return reviewRepository.findByProductOrderByIdDesc(product);
    }

}
