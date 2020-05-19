package com.ilqjx.service.impl;

import java.util.*;

import com.ilqjx.comparator.*;
import com.ilqjx.dao.OrderRepository;
import com.ilqjx.dao.ProductRepository;
import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;
import com.ilqjx.service.OrderItemService;
import com.ilqjx.service.ProductImageService;
import com.ilqjx.service.ProductService;
import com.ilqjx.service.ReviewService;
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
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ReviewService reviewService;

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
    public List<Product> listProductByCategory(Category category) {
        return productRepository.findByCategory(category);
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

    @Override
    public void setSaleCountAndReviewCount(List<Product> productList) {
        for (Product product : productList) {
            setSaleCountAndReviewCount(product);
        }
    }

    @Override
    public void setSaleCountAndReviewCount(Product product) {
        int saleCount = 0;
        List<OrderItem> orderItemList = orderItemService.listOrderItemByProduct(product);
        for (OrderItem orderItem : orderItemList) {
            if (null != orderItem.getOrder() && null != orderItem.getOrder().getPayDate()) {
                saleCount += orderItem.getNumber();
            }
        }
        int reviewCount = reviewService.countByProduct(product);
        product.setSaleCount(saleCount);
        product.setReviewCount(reviewCount);
    }

    @Override
    public void sortProduct(List<Product> productList, String sort) {
        if ("all".equals(sort)) {
            Collections.sort(productList, new ProductAllComparator());
        } else if ("date".equals(sort)) {
            Collections.sort(productList, new ProductDateComparator());
        } else if ("price".equals(sort)) {
            Collections.sort(productList, new ProductPriceComparator());
        } else if ("review".equals(sort)) {
            Collections.sort(productList, new ProductReviewComparator());
        } else if ("saleCount".equals(sort)) {
            Collections.sort(productList, new ProductSaleCountComparator());
        }
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        List<Product> productList = productRepository.findByKeyword(keyword);
        return productList;
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
