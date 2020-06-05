package com.ilqjx.service;

import java.util.List;

import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.Review;

public interface ReviewService {

    public Review saveReview(Review review);

    public int countByProduct(Product product);

    public List<Review> listReviewByProduct(Product product);

}
