package com.ilqjx.dao;

import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    public int countByProduct(Product product);

}
