package com.ilqjx.service;

import java.util.List;

import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.User;

public interface OrderItemService {

    public OrderItem saveOrderItem(OrderItem orderItem);

    public OrderItem getOrderItem(int id);

    public OrderItem getOrderItem(User user, Product product, Order order);

    public OrderItem updateOrderItem(OrderItem orderItem);

    public void deleteOrderItem(int id);

    public List<OrderItem> listOrderItem(User user, Order order);

    public List<OrderItem> listOrderItemByOrder(Order order);

    public List<OrderItem> listOrderItemByProduct(Product product);

    public void setOrderItemForOrder(Order order);

    public void setOrderItemForOrder(List<Order> orderList);

}
