package com.ilqjx.service;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Property;
import org.springframework.data.domain.Page;

public interface PropertyService {

    public Property saveProperty(Property property);

    public void deleteProperty(int id);

    public Property getProperty(int id);

    public Property updateProperty(Property property);

    public Page<Property> listPropertyByCategory(Category category, int start, int size);

}
