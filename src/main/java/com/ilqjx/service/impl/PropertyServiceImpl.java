package com.ilqjx.service.impl;

import java.util.Optional;

import com.ilqjx.dao.PropertyRepository;
import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Property;
import com.ilqjx.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "properties")
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    @CacheEvict(allEntries = true)
    public Property saveProperty(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteProperty(int id) {
        propertyRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "'properties-one-' + #p0")
    public Property getProperty(int id) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);
        try {
            propertyOptional.get();
        } catch (Exception e) {
            return null;
        }
        return propertyOptional.get();
    }

    @Override
    @CacheEvict(allEntries = true)
    public Property updateProperty(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    @Cacheable(key = "'properties-cid-' + #p0.id + '-page-' + #p1 + '-' + #p2")
    public Page<Property> listPropertyByCategory(Category category, int start, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Property> page = propertyRepository.findByCategory(pageable, category);
        return page;
    }

}
