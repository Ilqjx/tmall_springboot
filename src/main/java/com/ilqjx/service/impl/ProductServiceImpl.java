package com.ilqjx.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ilqjx.comparator.*;
import com.ilqjx.dao.ProductRepository;
import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;
import com.ilqjx.service.OrderItemService;
import com.ilqjx.service.ProductImageService;
import com.ilqjx.service.ProductService;
import com.ilqjx.service.ReviewService;
import com.ilqjx.util.PageUtil;
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
@CacheConfig(cacheNames = "products")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ReviewService reviewService;

    @Override
    @CacheEvict(allEntries = true)
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "'products-one-' + #p0")
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
    @CacheEvict(allEntries = true)
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Cacheable(key = "'products-cid-' + #p0.id")
    public List<Product> listProductByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    @Cacheable(key = "'products-cid-' + #p0.id + '-page-' + #p1 + '-' + #p2")
    public PageUtil<Product> listProductByCategory(Category category, int start, int size) {
        int navigatePages = 5;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Product> page = productRepository.findByCategory(category, pageable);
        return new PageUtil<>(page, navigatePages);
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
        // List<Product> productList = search(keyword, 0, 1000);
        return productList;
    }

    // public List<Product> search(String keyword, int start, int size) {
    //     initDatabase2ES();
    //     BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery("name", keyword));
    //     FunctionScoreQueryBuilder builder = QueryBuilders.functionScoreQuery(queryBuilder, ScoreFunctionBuilders.weightFactorFunction(100))
    //             .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
    //             .setMinScore(10);
    //     Sort sort = Sort.by(Sort.Direction.DESC, "id");
    //     Pageable pageable = PageRequest.of(start, size, sort);
    //     SearchQuery searchQuery = new NativeSearchQueryBuilder()
    //             .withPageable(pageable)
    //             .withQuery(builder).build();
    //     Page<Product> page = productESRepository.search(searchQuery);
    //     return page.getContent();
    // }

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

    /**
     * 初始化ES数据库
     */
    // private void initDatabase2ES() {
    //     Pageable pageable = PageRequest.of(0, 5);
    //     Page<Product> page = productESRepository.findAll(pageable);
    //     if (page.getContent().isEmpty()) {
    //         List<Product> productList = productRepository.findAll();
    //         for (Product product : productList) {
    //             productESRepository.save(product);
    //         }
    //     }
    // }

}
