package com.ilqjx.service;

import java.util.List;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    public Product saveProduct(Product product);

    public void deleteProduct(int id);

    public Product getProduct(int id);

    public Product updateProduct(Product product);

    public List<Product> listProductByCategory(Category category);

    public Page<Product> listProductByCategory(Category category, int start, int size);

    public void setProductForCategory(List<Category> categoryList);

    public void setProductByRowForCategory(List<Category> categoryList);

    public void setSaleCountAndReviewCount(List<Product> productList);

    public void setSaleCountAndReviewCount(Product product);

    public void sortProduct(List<Product> productList, String sort);

}
