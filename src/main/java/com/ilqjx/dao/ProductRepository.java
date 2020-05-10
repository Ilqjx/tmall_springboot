package com.ilqjx.dao;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public Page<Product> findByCategory(Category category, Pageable pageable);

}
