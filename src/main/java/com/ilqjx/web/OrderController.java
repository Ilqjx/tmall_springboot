package com.ilqjx.web;

import com.ilqjx.pojo.Order;
import com.ilqjx.service.OrderService;
import com.ilqjx.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public PageUtil<Order> listOrder(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        int navigatePages = 5;
        Page<Order> page = orderService.listOrder(start, size);
        PageUtil<Order> pageUtil = new PageUtil<>(page, navigatePages);
        System.out.println("pageUtil: " + pageUtil);
        return pageUtil;
    }

}
