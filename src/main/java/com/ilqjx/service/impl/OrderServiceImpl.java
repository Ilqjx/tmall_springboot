package com.ilqjx.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ilqjx.dao.OrderItemRepository;
import com.ilqjx.dao.OrderRepository;
import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.User;
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
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(int id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        try {
            orderOptional.get();
        } catch (Exception e) {
            return null;
        }
        return orderOptional.get();
    }

    @Override
    public Order updateOrderForFore(Order order) {
        return orderRepository.save(order);
    }

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
    public List<Order> listOrderWithoutDelete(User user) {
        String status = "delete";
        List<Order> orderList = orderRepository.findByUser(user, status);
        return orderList;
    }

    @Override
    public Order updateOrder(Order order) {
        order.setDeliveryDate(new Date());
        order.setStatus(OrderService.waitConfirm);
        return orderRepository.save(order);
    }

    @Override
    public void setTotalAndTotalNumber(List<Order> orderList) {
        for (Order order : orderList) {
            setTotalAndTotalNumber(order);
        }
    }

    @Override
    public void setTotalAndTotalNumber(Order order) {
        List<OrderItem> orderItemList = orderItemRepository.findByOrderOrderByIdDesc(order);
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

    @Override
    public void removeOrderFromOrderItem(List<Order> orderList) {
        for (Order order : orderList) {
            removeOrderFromOrderItem(order);
        }
    }

    @Override
    public void removeOrderFromOrderItem(Order order) {
        List<OrderItem> orderItemList = order.getOrderItemList();
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrder(null);
        }
    }

}
