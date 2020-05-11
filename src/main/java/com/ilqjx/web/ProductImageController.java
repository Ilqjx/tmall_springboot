package com.ilqjx.web;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.ProductImage;
import com.ilqjx.service.ProductImageService;
import com.ilqjx.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductService productService;

    @GetMapping("/products/{pid}/productimages")
    public List<ProductImage> listProductImage(@PathVariable int pid, @RequestParam String type) {
        Product product = productService.getProduct(pid);
        List<ProductImage> productImageList = productImageService.listProductImage(product, type);
        return productImageList;
    }

    @PostMapping("/productimages")
    public ProductImage saveProductImage(@RequestParam int pid, @RequestParam String type, MultipartFile file, HttpServletRequest request) {
        Product product = productService.getProduct(pid);
        ProductImage productImage = new ProductImage();
        productImage.setType(type);
        productImage.setProduct(product);
        return productImageService.saveProductImage(productImage, file, request);
    }

    @DeleteMapping("/productimages/{id}")
    public String deleteProductImage(@PathVariable int id, HttpServletRequest request) {
        productImageService.deleteProductImage(id, request);
        return null;
    }

}
