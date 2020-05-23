package com.ilqjx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.*;

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
        User u = userService.getUser(user);
        if (null == u) {
            return Result.fail("账号密码错误");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", u);
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

    @PostMapping("/forebuyone")
    public Result buyone(@RequestBody OrderItem orderItem, HttpServletRequest request) {
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

    @PostMapping("/foreaddCart")
    public Result addCart(@RequestBody OrderItem orderItem, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        OrderItem orderItemDB = orderItemService.getOrderItem(user, orderItem.getProduct(), null);
        if (orderItemDB == null) {
            orderItem.setUser(user);
            orderItemService.saveOrderItem(orderItem);
        } else {
            orderItemDB.setNumber(orderItemDB.getNumber() + orderItem.getNumber());
            orderItemService.updateOrderItem(orderItemDB);
        }
        return Result.success();
    }

    @GetMapping("/forebuy")
    public Result listOrderItem(String[] oiid) {
        List<OrderItem> orderItemList = new ArrayList<>();
        double total = 0;
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
        String status = "waitPay";
        Date createDate = new Date();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        order.setOrderCode(orderCode);
        order.setStatus(status);
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
        order.setStatus("waitDelivery");
        order.setPayDate(new Date());
        orderService.updateOrderForFore(order);
        return Result.success();
    }

    @GetMapping("/foregetorder/{id}")
    public Result getOrder(@PathVariable int id) {
        Order order = orderService.getOrder(id);
        return Result.success(order);
    }

    @GetMapping("/forelistOrderItem")
    public Result listOrderItem(HttpServletRequest request) {
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
        Product product = productService.getProduct(pid);
        OrderItem orderItem = orderItemService.getOrderItem(user, product, null);
        if (orderItem == null) {
            return Result.fail("订单项不存在");
        } else {
            orderItem.setNumber(num);
            orderItemService.updateOrderItem(orderItem);
            return Result.success();
        }
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
        return Result.success(orderList);
    }

    @DeleteMapping("/foredeleteOrder/{oid}")
    public Result deleteOrder(@PathVariable int oid) {
        Order order = orderService.getOrder(oid);
        order.setStatus("delete");
        orderService.updateOrderForFore(order);
        return Result.success();
    }

}
