package com.ilqjx.service;

import com.ilqjx.pojo.User;
import com.ilqjx.util.PageUtil;
import org.springframework.data.domain.Page;

public interface UserService {

    public User saveUser(User user);

    public User getUser(User user);

    public User getUser(int id);

    public User getUserByName(String name);

    public PageUtil<User> listUser(int start, int size);

    public boolean isExist(String name);

}
