package com.ilqjx.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ilqjx.dao.ProductRepository;
import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Product;
import com.ilqjx.service.CategoryService;
import com.ilqjx.service.ProductImageService;
import com.ilqjx.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public Product saveProduct(Product product) {
        product.setCreateDate(new Date());
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getProduct(int id) {
        Optional<Product> productOptional = productRepository.findById(id);
        try {
            productOptional.get();
        } catch (Exception e) {
            return null;
        }
        return productOptional.get();
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Page<Product> listProductByCategory(Category category, int start, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Product> page = productRepository.findByCategory(category, pageable);
        return page;
    }

    @Override
    public void setProductForCategory(List<Category> categoryList) {
        for (Category category : categoryList) {
            setProductForCategory(category);
        }
    }

    @Override
    public void setProductByRowForCategory(List<Category> categoryList) {
        for (Category category : categoryList) {
            setProductByRowForCategory(category);
        }
    }

    private void setProductForCategory(Category category) {
        List<Product> productList = productRepository.findByCategoryOrderByIdDesc(category);
        productImageService.setFirstProductImageForProduct(productList);
        category.setProductList(productList);
    }

    private void setProductByRowForCategory(Category category) {
        int productNumberEachRow = 8;
        List<Product> productList = category.getProductList();
        List<List<Product>> productListByRow = new ArrayList<>();
        for (int i = 0; i < productList.size(); i += productNumberEachRow) {
            int startIndex = i;
            int endIndex = i + productNumberEachRow;
            if (endIndex > productList.size()) {
                endIndex = productList.size();
            }
            List<Product> eachProductListByRow = productList.subList(startIndex, endIndex);
            productListByRow.add(eachProductListByRow);
        }
        category.setProductListByRow(productListByRow);
    }


}
