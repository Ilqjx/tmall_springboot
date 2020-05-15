package com.ilqjx.service;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Product;
import com.ilqjx.util.PageUtil;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {

    public Category saveCategory(Category category, MultipartFile file, HttpServletRequest request);

    public void deleteCategory(int id);

    public Category getCategory(int id);

    public Category updateCategory(Category category, MultipartFile file, HttpServletRequest request);

    public List<Category> listCategory();

    public PageUtil listCategory(int start, int size, int navigatePages);

    public void removeCategoryFromProduct(List<Category> categoryList);

}
