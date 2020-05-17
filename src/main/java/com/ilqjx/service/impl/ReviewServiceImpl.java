package com.ilqjx.service.impl;

import com.ilqjx.dao.ReviewRepository;
import com.ilqjx.pojo.Product;
import com.ilqjx.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public int countByProduct(Product product) {
        return reviewRepository.countByProduct(product);
    }

}
