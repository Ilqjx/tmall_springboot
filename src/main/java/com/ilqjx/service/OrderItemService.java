package com.ilqjx.service;

import java.util.List;

import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;

public interface OrderItemService {

    public OrderItem saveOrderItem(OrderItem orderItem);

    public OrderItem getOrderItem(int id);

    public OrderItem updateOrderItem(OrderItem orderItem);

    public List<OrderItem> listOrderItemByOrder(Order order);

    public List<OrderItem> listOrderItemByProduct(Product product);

    public void setOrderItemForOrder(List<Order> orderList);

}
