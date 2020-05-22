package com.ilqjx.web;

import com.ilqjx.pojo.Order;
import com.ilqjx.service.OrderItemService;
import com.ilqjx.service.OrderService;
import com.ilqjx.util.PageUtil;
import com.ilqjx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/orders")
    public PageUtil<Order> listOrder(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        int navigatePages = 5;
        Page<Order> page = orderService.listOrder(start, size);
        orderItemService.setOrderItemForOrder(page.getContent());
        PageUtil<Order> pageUtil = new PageUtil<>(page, navigatePages);
        return pageUtil;
    }

    @PutMapping("/orders")
    public Object updateOrder(@RequestBody Order order) {
        orderService.updateOrder(order);
        return Result.success();
    }

}
