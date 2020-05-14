package com.ilqjx.service.impl;

import java.util.Date;
import java.util.List;

import com.ilqjx.dao.OrderItemRepository;
import com.ilqjx.dao.OrderRepository;
import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.service.OrderService;
import com.ilqjx.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductImageService productImageService;

    @Override
    public Page<Order> listOrder(int start, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        setTotalAndTotalNumber(page.getContent());
        removeOrderFromOrderItem(page.getContent());
        return page;
    }

    @Override
    public Order updateOrder(Order order) {
        order.setDeliveryDate(new Date());
        order.setStatus(OrderService.waitConfirm);
        return orderRepository.save(order);
    }

    private void setTotalAndTotalNumber(List<Order> orderList) {
        for (Order order : orderList) {
            setTotalAndTotalNumber(order);
        }
    }

    private void setTotalAndTotalNumber(Order order) {
        List<OrderItem> orderItemList = orderItemRepository.findByOrder(order);
        productImageService.setFirstProductImageForOrderItem(orderItemList);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem orderItem : orderItemList) {
            totalNumber += orderItem.getNumber();
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
        }
        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItemList(orderItemList);
    }

    private void removeOrderFromOrderItem(List<Order> orderList) {
        for (Order order : orderList) {
            removeOrderFromOrderItem(order);
        }
    }

    private void removeOrderFromOrderItem(Order order) {
        List<OrderItem> orderItemList = order.getOrderItemList();
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrder(null);
        }
    }

}
