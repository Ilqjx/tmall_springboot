package com.ilqjx.dao;

import java.util.List;

import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    public List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);

}
