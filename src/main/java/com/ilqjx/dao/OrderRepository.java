package com.ilqjx.dao;

import java.util.List;

import com.ilqjx.pojo.Order;
import com.ilqjx.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status <> :status ORDER BY o.createDate DESC")
    public List<Order> findByUser(@Param("user") User user, @Param("status") String status);

}
