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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "orderItems")
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductImageService productImageService;

    @Override
    @CacheEvict(allEntries = true)
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Cacheable(key = "'orderItems-one-' + #p0")
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
    @Cacheable(key = "'orderItems-uid-' + #p0.id + '-pid-' + #p1.id")
    public OrderItem getOrderItem(User user, Product product, Order order) {
        OrderItem orderItem = orderItemRepository.findByUserAndProductAndOrder(user, product, order);
        return orderItem;
    }

    @Override
    @CacheEvict(allEntries = true)
    public OrderItem updateOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteOrderItem(int id) {
        orderItemRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "'orderItems-uid-' + #p0.id")
    public List<OrderItem> listOrderItem(User user, Order order) {
        return orderItemRepository.findByUserAndOrder(user, order);
    }

    @Override
    @Cacheable(key = "'orderItems-oid-' + #p0.id")
    public List<OrderItem> listOrderItemByOrder(Order order) {
        return orderItemRepository.findByOrderOrderByIdDesc(order);
    }

    @Override
    @Cacheable(key = "'orderItems-pid-' + #p0.id")
    public List<OrderItem> listOrderItemByProduct(Product product) {
        return orderItemRepository.findByProduct(product);
    }

    @Override
    public void setOrderItemForOrder(Order order) {
        List<OrderItem> orderItemList = listOrderItemByOrder(order);
        productImageService.setFirstProductImageForOrderItem(orderItemList);
        order.setOrderItemList(orderItemList);
    }

    @Override
    public void setOrderItemForOrder(List<Order> orderList) {
        for (Order order : orderList) {
            setOrderItemForOrder(order);
        }
    }

}
