package com.ilqjx.web;

import java.util.List;

import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.PropertyValue;
import com.ilqjx.service.ProductService;
import com.ilqjx.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PropertyValueController {

    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ProductService productService;

    @GetMapping("/products/{pid}/propertyvalues")
    public List<PropertyValue> listPropertyValue(@PathVariable int pid) {
        Product product = productService.getProduct(pid);
        List<PropertyValue> propertyValueList = propertyValueService.listPropertyValueByProduct(product);
        return propertyValueList;
    }

    @PutMapping("/propertyvalues")
    public PropertyValue updatePropertyValue(@RequestBody PropertyValue propertyValue) {
        return propertyValueService.updatePropertyValue(propertyValue);
    }

}
