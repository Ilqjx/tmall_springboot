package com.ilqjx.service;

import com.ilqjx.pojo.Order;
import org.springframework.data.domain.Page;

public interface OrderService {

    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    public Order saveOrder(Order order);

    public Order getOrder(int id);

    public Order updateOrderForFore(Order order);

    public Order updateOrder(Order order);

    public Page<Order> listOrder(int start, int size);

}
