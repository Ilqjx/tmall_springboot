package com.ilqjx.web;

import javax.servlet.http.HttpServletRequest;

import com.ilqjx.pojo.Category;
import com.ilqjx.service.CategoryService;
import com.ilqjx.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public PageUtil listCategory(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) {
        int navigatePages = 5;
        PageUtil pageUtil = categoryService.listCategory(start, size, navigatePages);
        return pageUtil;
    }

    @PostMapping("/categories")
    public Category saveCategory(Category category, MultipartFile file, HttpServletRequest request) {
        return categoryService.saveCategory(category, file, request);
    }

    @DeleteMapping("/categories/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return null;
    }

    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable int id) {
        return categoryService.getCategory(id);
    }

    @PutMapping("/categories")
    public Category updateCategory(Category category, MultipartFile file, HttpServletRequest request) {
        return categoryService.updateCategory(category, file, request);
    }

}
