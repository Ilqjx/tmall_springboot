package com.ilqjx.dao;

import java.util.List;

import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    public List<OrderItem> findByOrder(Order order);

}
