package com.ilqjx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ilqjx.pojo.*;
import com.ilqjx.service.*;
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
    @Autowired
    private PropertyValueService propertyValueService;
    @Autowired
    private ReviewService reviewService;

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
    public Result getProduct(@PathVariable int id) {
        Product product = productService.getProduct(id);
        productImageService.setFirstProductImage(product);
        productService.setSaleCountAndReviewCount(product);

        List<ProductImage> singleProductImageList = productImageService.listProductImage(product, "single");
        List<ProductImage> detailProductImageList = productImageService.listProductImage(product, "detail");
        List<PropertyValue> propertyValueList = propertyValueService.listPropertyValueByProduct(product);
        List<Review> reviewList = reviewService.listReviewByProduct(product);

        Map<Object, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("singleProductImages", singleProductImageList);
        map.put("detailProductImages", detailProductImageList);
        map.put("propertyValues", propertyValueList);
        map.put("reviews", reviewList);

        return Result.success(map);
    }

    @GetMapping("/forecheckLogin")
    public Result checkLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (null == user) {
            return Result.fail("用户未登录");
        } else {
            return Result.success();
        }
    }

    @GetMapping("/forecategory/{id}")
    public Result getCategory(@PathVariable int id, @RequestParam(value = "sort", defaultValue = "all") String sort) {
        Category category = categoryService.getCategory(id);
        List<Product> productList = productService.listProductByCategory(category);
        productImageService.setFirstProductImageForProduct(productList);
        productService.setSaleCountAndReviewCount(productList);
        productService.sortProduct(productList, sort);
        category.setProductList(productList);
        categoryService.removeCategoryFromProduct(category);

        Map<Object, Object> map = new HashMap<>();
        map.put("category", category);
        map.put("sort", sort);

        return Result.success(map);
    }

    @PostMapping("/foresearch")
    public Result search(@RequestParam String keyword) {
        if (keyword == null) {
            keyword = "";
        }
        keyword = "%" + keyword + "%";
        List<Product> productList = productService.searchProduct(keyword);
        productService.setSaleCountAndReviewCount(productList);
        productImageService.setFirstProductImageForProduct(productList);
        return Result.success(productList);
    }

}
