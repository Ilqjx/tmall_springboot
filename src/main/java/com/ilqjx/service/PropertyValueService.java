package com.ilqjx.service;

import java.util.List;

import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.PropertyValue;

public interface PropertyValueService {

    public PropertyValue savePropertyValue(PropertyValue propertyValue);

    public PropertyValue updatePropertyValue(PropertyValue propertyValue);

    public PropertyValue getPropertyValue(int id);

    public List<PropertyValue> listPropertyValueByProduct(Product product);

}
