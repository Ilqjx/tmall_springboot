package com.ilqjx.dao;

import java.util.List;

import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    public List<OrderItem> findByOrderOrderByIdDesc(Order order);

    public List<OrderItem> findByProduct(Product product);

}
