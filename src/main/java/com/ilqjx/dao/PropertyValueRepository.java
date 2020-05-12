package com.ilqjx.dao;

import java.util.List;

import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.Property;
import com.ilqjx.pojo.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyValueRepository extends JpaRepository<PropertyValue, Integer> {

    public List<PropertyValue> findByProduct(Product product);

    public PropertyValue findByProductAndProperty(Product product, Property property);

}
