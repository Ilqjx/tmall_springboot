package com.ilqjx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.ProductImage;
import com.ilqjx.pojo.User;
import com.ilqjx.service.CategoryService;
import com.ilqjx.service.ProductImageService;
import com.ilqjx.service.ProductService;
import com.ilqjx.service.UserService;
import com.ilqjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/forehome")
    public List<Category> forehome() {
        List<Category> categoryList = categoryService.listCategory();
        productService.setProductForCategory(categoryList);
        productService.setProductByRowForCategory(categoryList);
        categoryService.removeCategoryFromProduct(categoryList);
        return categoryList;
    }

    @PostMapping("/foreregister")
    public Result register(@RequestBody User user) {
        String name = HtmlUtils.htmlEscape(user.getName());
        user.setName(name);
        if (userService.isExist(user.getName())) {
            return Result.fail("用户名已存在，请重新输入");
        }
        userService.saveUser(user);
        return Result.success();
    }

    @PostMapping("/forelogin")
    public Result login(@RequestBody User user, HttpServletRequest request) {
        String name = HtmlUtils.htmlEscape(user.getName());
        user.setName(name);
        if (null == userService.getUser(user)) {
            return Result.fail("账号密码错误");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return Result.success();
        }
    }

    @GetMapping("/foreproduct/{id}")
    public Product getProduct(@PathVariable int id) {
        Product product = productService.getProduct(id);
        productImageService.setFirstProductImage(product);
        productService.setSaleCountAndReviewCount(product);
        return product;
    }

    @GetMapping("/foreproductsingleimage/{pid}")
    public List<ProductImage> listProductSingleImage(@PathVariable int pid) {
        Product product = productService.getProduct(pid);
        String type = "single";
        List<ProductImage> singleProductImageList = productImageService.listProductImage(product, type);
        return singleProductImageList;
    }

}
