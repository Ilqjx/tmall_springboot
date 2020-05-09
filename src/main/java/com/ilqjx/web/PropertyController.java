package com.ilqjx.web;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Property;
import com.ilqjx.service.CategoryService;
import com.ilqjx.service.PropertyService;
import com.ilqjx.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
public class PropertyController {
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories/{cid}/properties")
    public PageUtil<Property> listProperty(@PathVariable int cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        int navigatePages = 5;
        Category category = categoryService.getCategory(cid);
        Page<Property> page = propertyService.listPropertyByCategory(category, start, size, navigatePages);
        PageUtil<Property> pageUtil = new PageUtil<>(page, navigatePages);
        return pageUtil;
    }

    @PostMapping("/properties")
    public Property saveProperty(@RequestBody Property property) {
        return propertyService.saveProperty(property);
    }

    @DeleteMapping("/properties/{id}")
    public String deleteProperty(@PathVariable int id) {
        propertyService.deleteProperty(id);
        return null;
    }

    @GetMapping("/properties/{id}")
    public Property getProperty(@PathVariable("id") int id) {
        return propertyService.getProperty(id);
    }

    @PutMapping("/properties")
    public Property updateProperty(@RequestBody Property property) {
        return propertyService.updateProperty(property);
    }

}
