package com.ilqjx.dao;

import com.ilqjx.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByName(String name);

    public User getUserByNameAndPassword(String name, String password);

}
