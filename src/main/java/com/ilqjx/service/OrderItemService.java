package com.ilqjx.service;

import java.util.List;

import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;

public interface OrderItemService {

    public List<OrderItem> listOrderItemByOrder(Order order);

    public List<OrderItem> listOrderItemByProduct(Product product);

}
