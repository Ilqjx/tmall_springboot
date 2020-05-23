package com.ilqjx.dao;

import java.util.List;

import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.OrderItem;
import com.ilqjx.pojo.Product;
import com.ilqjx.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    public List<OrderItem> findByOrderOrderByIdDesc(Order order);

    public List<OrderItem> findByProduct(Product product);

    public List<OrderItem> findByUserAndOrder(User user, Order order);

    public OrderItem findByUserAndProductAndOrder(User user, Product product, Order order);

}
