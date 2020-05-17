package com.ilqjx.service;

import com.ilqjx.pojo.User;
import org.springframework.data.domain.Page;

public interface UserService {

    public User saveUser(User user);

    public User getUser(User user);

    public User getUserByName(String name);

    public Page<User> listUser(int start, int size);

    public boolean isExist(String name);

}
