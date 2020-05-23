package com.ilqjx.service.impl;

import java.util.List;
import java.util.Optional;

import com.ilqjx.dao.OrderItemRepository;
import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.User;
import com.ilqjx.service.OrderItemService;
import com.ilqjx.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductImageService productImageService;

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem getOrderItem(int id) {
        Optional<OrderItem> orderItemOptional = orderItemRepository.findById(id);
        try {
            orderItemOptional.get();
        } catch (Exception e) {
            return null;
        }
        return orderItemOptional.get();
    }

    @Override
    public OrderItem getOrderItem(User user, Product product, Order order) {
        OrderItem orderItem = orderItemRepository.findByUserAndProductAndOrder(user, product, order);
        return orderItem;
    }

    @Override
    public OrderItem updateOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteOrderItem(int id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItem> listOrderItem(User user, Order order) {
        return orderItemRepository.findByUserAndOrder(user, order);
    }

    @Override
    public List<OrderItem> listOrderItemByOrder(Order order) {
        return orderItemRepository.findByOrderOrderByIdDesc(order);
    }

    @Override
    public List<OrderItem> listOrderItemByProduct(Product product) {
        return orderItemRepository.findByProduct(product);
    }

    @Override
    public void setOrderItemForOrder(List<Order> orderList) {
        for (Order order : orderList) {
            List<OrderItem> orderItemList = listOrderItemByOrder(order);
            productImageService.setFirstProductImageForOrderItem(orderItemList);
            order.setOrderItemList(orderItemList);
        }
    }

}
