package com.ilqjx.web;

import java.util.List;

import com.ilqjx.pojo.Category;
import com.ilqjx.pojo.User;
import com.ilqjx.service.CategoryService;
import com.ilqjx.service.ProductService;
import com.ilqjx.service.UserService;
import com.ilqjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
public class ForeRESTController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

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
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        if (userService.isExist(user.getName())) {
            return Result.fail("用户名已存在，请重新输入");
        }
        userService.saveUser(user);
        return Result.success();
    }

}
