package com.ilqjx.service.impl;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.ilqjx.dao.CategoryRepository;
import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Product;
import com.ilqjx.service.CategoryService;
import com.ilqjx.util.ImageUtil;
import com.ilqjx.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category, MultipartFile file, HttpServletRequest request) {
        Category tempCategory = categoryRepository.save(category);
        try {
            saveOrUpdateImage(tempCategory, file, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempCategory;
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategory(int id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        try {
            categoryOptional.get();
        } catch (Exception e) {
            return null;
        }
        return categoryOptional.get();
    }

    @Override
    public Category updateCategory(Category category, MultipartFile file, HttpServletRequest request) {
        if (file != null) {
            try {
                saveOrUpdateImage(category, file, request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> listCategory() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return categoryRepository.findAll(sort);
    }

    @Override
    public PageUtil listCategory(int start, int size, int navigatePages) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        return new PageUtil(page, navigatePages);
    }

    @Override
    public void removeCategoryFromProduct(List<Category> categoryList) {
        for (Category category : categoryList) {
            removeCategoryFromProduct(category);
        }
    }

    @Override
    public void removeCategoryFromProduct(Category category) {
        List<Product> productList = category.getProductList();
        if (null != productList) {
            for (Product product : productList) {
                product.setCategory(null);
            }
        }

        List<List<Product>> productListByRow = category.getProductListByRow();
        if (null != productListByRow) {
            for (List<Product> products : productListByRow) {
                for (Product product : products) {
                    product.setCategory(null);
                }
            }
        }
    }

    private void saveOrUpdateImage(Category category, MultipartFile file, HttpServletRequest request) throws IOException {
        File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
        File image = new File(imageFolder, category.getId() + ".jpg");
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        file.transferTo(image);
        BufferedImage bufferedImage = ImageUtil.chang2jpg(image);
        ImageIO.write(bufferedImage, "jpg", image);
    }

}
