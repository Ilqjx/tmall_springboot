package com.ilqjx.service;

import java.util.List;

import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;

public interface OrderItemService {

    public List<OrderItem> listOrderItemByOrder(Order order);

}
