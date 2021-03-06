package com.ilqjx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.*;

import com.ilqjx.pojo.*;
import com.ilqjx.service.*;
import com.ilqjx.util.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
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
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

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
        if (userService.isExist(name)) {
            return Result.fail("用户名已存在，请重新输入");
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        String password = user.getPassword();
        String algorithmName = "md5";
        int times = 2;
        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

        user.setName(name);
        user.setPassword(encodedPassword);
        user.setSalt(salt);

        userService.saveUser(user);
        return Result.success();
    }

    @PostMapping("/forelogin")
    public Result login(@RequestBody User user, HttpServletRequest request) {
        String name = HtmlUtils.htmlEscape(user.getName());
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, user.getPassword());
        try {
            subject.login(token);
            HttpSession session = request.getSession();
            User u = userService.getUserByName(name);
            session.setAttribute("user", u);
            return Result.success();
        } catch (AuthenticationException e) {
            return Result.fail("账号密码错误");
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
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return Result.success();
        } else {
            return Result.fail("用户未登录");
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

    @PostMapping("/forebuyone")
    public Result buyone(@RequestBody OrderItem orderItem, HttpServletRequest request) {
        return buyoneAndAddCart(orderItem, request);
    }

    @PostMapping("/foreaddCart")
    public Result addCart(@RequestBody OrderItem orderItem, HttpServletRequest request) {
        return buyoneAndAddCart(orderItem, request);
    }

    @GetMapping("/forebuy")
    public Result listOrderItem(String[] oiid) {
        List<OrderItem> orderItemList = new ArrayList<>();
        float total = 0;
        for (String i : oiid) {
            int id = Integer.parseInt(i);
            OrderItem orderItem = orderItemService.getOrderItem(id);
            total += orderItem.getProduct().getPromotePrice() * orderItem.getNumber();
            orderItemList.add(orderItem);
        }
        productImageService.setFirstProductImageForOrderItem(orderItemList);

        Map<Object, Object> map = new HashMap<>();
        map.put("orderItems", orderItemList);
        map.put("total", total);

        return Result.success(map);
    }

    @PostMapping("/forecreateOrder")
    public Result createOrder(@RequestBody Order order, String[] oiid, HttpServletRequest request) {
        long time = System.currentTimeMillis();
        double randomDigit = Math.ceil(Math.random() * 9000) + 1000;
        long lastSuffix = new Double(randomDigit).longValue();
        String orderCode = "" + time + lastSuffix;
        Date createDate = new Date();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        order.setOrderCode(orderCode);
        order.setStatus(OrderService.waitPay);
        order.setCreateDate(createDate);
        order.setUser(user);

        Order returnOrder = orderService.saveOrder(order);

        for (String i : oiid) {
            int id = Integer.parseInt(i);
            OrderItem orderItem = orderItemService.getOrderItem(id);
            orderItem.setOrder(order);
            orderItemService.updateOrderItem(orderItem);
        }

        return Result.success(returnOrder);
    }

    @GetMapping("/forepayed/{oid}")
    public Result payed(@PathVariable int oid) {
        Order order = orderService.getOrder(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.updateOrderForFore(order);
        return Result.success();
    }

    @GetMapping("/foregetorder/{id}")
    public Result getOrder(@PathVariable int id) {
        Order order = orderService.getOrder(id);
        return Result.success(order);
    }

    @GetMapping("/forecart")
    public Result cart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<OrderItem> orderItemList = orderItemService.listOrderItem(user, null);
        productImageService.setFirstProductImageForOrderItem(orderItemList);
        return Result.success(orderItemList);
    }

    @PutMapping("/forechangeOrderItem")
    public Result changeOrderItem(@RequestParam int pid, @RequestParam int num, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.fail("未登录");
        }
        Product product = productService.getProduct(pid);
        OrderItem orderItem = orderItemService.getOrderItem(user, product, null);
        orderItem.setNumber(num);
        orderItemService.updateOrderItem(orderItem);
        return Result.success();
    }

    @DeleteMapping("/foredeleteOrderItem/{id}")
    public Result deleteOrderItem(@PathVariable int id) {
        orderItemService.deleteOrderItem(id);
        return Result.success();
    }

    @GetMapping("/forelistOrder")
    public Result listOrder(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Order> orderList = orderService.listOrderWithoutDelete(user);
        orderService.setTotalAndTotalNumber(orderList);
        orderService.removeOrderFromOrderItem(orderList);
        return Result.success(orderList);
    }

    @DeleteMapping("/foredeleteOrder/{oid}")
    public Result deleteOrder(@PathVariable int oid) {
        Order order = orderService.getOrder(oid);
        order.setStatus(OrderService.delete);
        orderService.updateOrderForFore(order);
        return Result.success();
    }

    @GetMapping("/foreconfirmPay/{oid}")
    public Result confirmPay(@PathVariable int oid) {
        Order order = orderService.getOrder(oid);
        orderItemService.setOrderItemForOrder(order);
        orderService.setTotalAndTotalNumber(order);
        orderService.removeOrderFromOrderItem(order);
        return Result.success(order);
    }

    @PutMapping("/foreorderConfirmed/{oid}")
    public Result orderConfirmed(@PathVariable int oid) {
        Order order = orderService.getOrder(oid);
        order.setConfirmDate(new Date());
        order.setStatus(OrderService.waitReview);
        orderService.updateOrderForFore(order);
        return Result.success();
    }

    @GetMapping("/forereview/{oid}")
    public Result review(@PathVariable int oid) {
        Order order = orderService.getOrder(oid);
        Product product = orderItemService.listOrderItemByOrder(order).get(0).getProduct();
        productService.setSaleCountAndReviewCount(product);
        productImageService.setFirstProductImage(product);

        Map<Object, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("product", product);
        return Result.success(map);
    }

    @PostMapping("/foredoReview/{oid}")
    public Result doReview(@RequestBody Review review, @PathVariable int oid, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        review.setContent(HtmlUtils.htmlEscape(review.getContent()));
        review.setUser(user);
        review.setCreateDate(new Date());
        reviewService.saveReview(review);

        Order order = orderService.getOrder(oid);
        order.setStatus(OrderService.finish);
        orderService.updateOrderForFore(order);
        return Result.success();
    }

    @GetMapping("/forelistReview/{pid}")
    public Result listReview(@PathVariable int pid) {
        Product product = productService.getProduct(pid);
        List<Review> reviewList = reviewService.listReviewByProduct(product);
        return Result.success(reviewList);
    }

    private Result buyoneAndAddCart(OrderItem orderItem, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        OrderItem orderItemDB = orderItemService.getOrderItem(user, orderItem.getProduct(), null);
        OrderItem oi;
        if (orderItemDB == null) {
            orderItem.setUser(user);
            oi = orderItemService.saveOrderItem(orderItem);
        } else {
            orderItemDB.setNumber(orderItemDB.getNumber() + orderItem.getNumber());
            oi = orderItemService.updateOrderItem(orderItemDB);
        }
        return Result.success(oi);
    }

}
