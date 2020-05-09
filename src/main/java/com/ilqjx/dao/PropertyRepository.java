package com.ilqjx.dao;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Integer> {

    public Page<Property> findByCategory(Pageable pageable, Category category);

}
