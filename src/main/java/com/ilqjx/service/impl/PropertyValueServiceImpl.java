package com.ilqjx.service.impl;

import java.util.List;
import java.util.Optional;

import com.ilqjx.dao.PropertyRepository;
import com.ilqjx.dao.PropertyValueRepository;
import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.Property;
import com.ilqjx.pojo.PropertyValue;
import com.ilqjx.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "propertyValues")
public class PropertyValueServiceImpl implements PropertyValueService {

    @Autowired
    private PropertyValueRepository propertyValueRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    @CacheEvict(allEntries = true)
    public PropertyValue savePropertyValue(PropertyValue propertyValue) {
        return propertyValueRepository.save(propertyValue);
    }

    @Override
    @CacheEvict(allEntries = true)
    public PropertyValue updatePropertyValue(PropertyValue propertyValue) {
        return propertyValueRepository.save(propertyValue);
    }

    @Override
    @Cacheable(key = "'propertyValues-one-' + #p0")
    public PropertyValue getPropertyValue(int id) {
        Optional<PropertyValue> propertyValueOptional = propertyValueRepository.findById(id);
        try {
            propertyValueOptional.get();
        } catch (Exception e) {
            return null;
        }
        return propertyValueOptional.get();
    }

    @Override
    @Cacheable(key = "'propertyValues-pid-' + #p0.id")
    public List<PropertyValue> listPropertyValueByProduct(Product product) {
        setPropertyValueForProduct(product);
        return propertyValueRepository.findByProduct(product);
    }

    private void setPropertyValueForProduct(Product product) {
        // 获取该产品所在分类的所有属性
        List<Property> propertyList = propertyRepository.findByCategory(product.getCategory());
        for (Property property : propertyList) {
            PropertyValue propertyValue = propertyValueRepository.findByProductAndProperty(product, property);
            if (null == propertyValue) {
                PropertyValue pv = new PropertyValue();
                pv.setProduct(product);
                pv.setProperty(property);
                savePropertyValue(pv);
            }
        }
    }

}
