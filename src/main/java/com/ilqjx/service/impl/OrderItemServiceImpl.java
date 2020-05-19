package com.ilqjx.service.impl;

import java.util.List;

import com.ilqjx.dao.OrderItemRepository;
import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;
import com.ilqjx.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> listOrderItemByOrder(Order order) {
        return orderItemRepository.findByOrderOrderByIdDesc(order);
    }

    @Override
    public List<OrderItem> listOrderItemByProduct(Product product) {
        return orderItemRepository.findByProduct(product);
    }

}
