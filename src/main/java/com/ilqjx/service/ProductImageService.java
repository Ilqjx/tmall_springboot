package com.ilqjx.service;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {

    public ProductImage saveProductImage(ProductImage productImage, MultipartFile file, HttpServletRequest request);

    public void deleteProductImage(int id, HttpServletRequest request);

    public ProductImage getProductImage(int id);

    public List<ProductImage> listProductImage(Product product, String type);

    public void setFirstProductImage(Product product);

    public void setFirstProductImage(Page<Product> page);

    public void setFirstProductImageForOrderItem(List<OrderItem> orderItemList);

    public void setFirstProductImageForProduct(List<Product> productList);

}
