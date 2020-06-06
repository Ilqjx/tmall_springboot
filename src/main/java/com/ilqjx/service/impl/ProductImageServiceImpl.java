package com.ilqjx.service.impl;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.ilqjx.dao.ProductImageRepository;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.ProductImage;
import com.ilqjx.service.ProductImageService;
import com.ilqjx.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@CacheConfig(cacheNames = "productImages")
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    @CacheEvict(allEntries = true)
    public ProductImage saveProductImage(ProductImage productImage, MultipartFile file, HttpServletRequest request) {
        ProductImage tempProductImage = productImageRepository.save(productImage);
        try {
            uploadProductImage(tempProductImage, file, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempProductImage;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteProductImage(int id, HttpServletRequest request) {
        ProductImage productImage = getProductImage(id);
        deleteProductImage(productImage, request);
        productImageRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "'productImages-one-' + #p0")
    public ProductImage getProductImage(int id) {
        Optional<ProductImage> productImageOptional = productImageRepository.findById(id);
        try {
            productImageOptional.get();
        } catch (Exception e) {
            return null;
        }
        return productImageOptional.get();
    }

    @Override
    @Cacheable(key = "'productImages-' + #p1 + '-pid-' + #p0.id")
    public List<ProductImage> listProductImage(Product product, String type) {
        return productImageRepository.findByProductAndTypeOrderByIdDesc(product, type);
    }

    @Override
    public void setFirstProductImage(Product product) {
        String type = "single";
        List<ProductImage> productImageList = listProductImage(product, type);
        if (!productImageList.isEmpty()) {
            product.setFirstProductImage(productImageList.get(0));
        } else {
            product.setFirstProductImage(new ProductImage());
        }
    }

    @Override
    public void setFirstProductImageForOrderItem(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            setFirstProductImage(orderItem.getProduct());
        }
    }

    @Override
    public void setFirstProductImageForProduct(List<Product> productList) {
        for (Product product : productList) {
            setFirstProductImage(product);
        }
    }

    private void uploadProductImage(ProductImage productImage, MultipartFile file, HttpServletRequest request) throws IOException {
        File imageFolder;
        if ("single".equals(productImage.getType())) {
            imageFolder = new File(request.getServletContext().getRealPath("img/productSingleImage"));
        } else {
            imageFolder = new File(request.getServletContext().getRealPath("img/productDetailImage"));
        }
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        File image = new File(imageFolder, productImage.getId() + ".jpg");
        file.transferTo(image);
        BufferedImage bufferedImage = ImageUtil.chang2jpg(image);
        ImageIO.write(bufferedImage, "jpg", image);

        if ("single".equals(productImage.getType())) {
            File smallImageFolder = new File(request.getServletContext().getRealPath("img/productSingleImage_small"));
            File middleImageFolder = new File(request.getServletContext().getRealPath("img/productSingleImage_middle"));
            if (!smallImageFolder.exists()) {
                smallImageFolder.mkdirs();
            }
            if (!middleImageFolder.exists()) {
                middleImageFolder.mkdirs();
            }
            File smallImage = new File(smallImageFolder, productImage.getId() + ".jpg");
            File middleImage = new File(middleImageFolder, productImage.getId() + ".jpg");
            ImageUtil.resizeImage(image, 56, 56, smallImage);
            ImageUtil.resizeImage(image, 217, 190, middleImage);
        }
    }

    private void deleteProductImage(ProductImage productImage, HttpServletRequest request) {
        if ("single".equals(productImage.getType())) {
            File imageFolder = new File(request.getServletContext().getRealPath("img/productSingleImage"));
            File smallImageFolder = new File(request.getServletContext().getRealPath("img/productSingleImage_small"));
            File middleImageFolder = new File(request.getServletContext().getRealPath("img/productSingleImage_middle"));
            File image = new File(imageFolder, productImage.getId() + ".jpg");
            File smallImage = new File(smallImageFolder, productImage.getId() + ".jpg");
            File middleImage = new File(middleImageFolder, productImage.getId() + ".jpg");
            image.delete();
            smallImage.delete();
            middleImage.delete();
        } else {
            File imageFolder = new File(request.getServletContext().getRealPath("img/productDetailImage"));
            File image = new File(imageFolder, productImage.getId() + ".jpg");
            image.delete();
        }
    }

}
