package com.ilqjx.service.impl;

import java.util.Date;
import java.util.Optional;

import com.ilqjx.dao.ProductRepository;
import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Product;
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


}
