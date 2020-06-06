package com.ilqjx.web;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Product;
import com.ilqjx.service.CategoryService;
import com.ilqjx.service.ProductImageService;
import com.ilqjx.service.ProductService;
import com.ilqjx.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/categories/{cid}/products")
    public PageUtil<Product> listProduct(@PathVariable int cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        int navigatePages = 5;
        Category category = categoryService.getCategory(cid);
        PageUtil<Product> pageUtil = productService.listProductByCategory(category, start, size);
        productImageService.setFirstProductImageForProduct(pageUtil.getContent());
        return pageUtil;
    }

    @PostMapping("/products")
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return null;
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable int id) {
        return productService.getProduct(id);
    }

    @PutMapping("/products")
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

}
