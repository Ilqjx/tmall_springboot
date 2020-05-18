package com.ilqjx.dao;

import java.util.List;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    public List<Product> findByCategoryOrderByIdDesc(Category category);

    public List<Product> findByCategory(Category category);

    public Page<Product> findByCategory(Category category, Pageable pageable);

}
