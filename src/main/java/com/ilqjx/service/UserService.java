package com.ilqjx.service;

import com.ilqjx.pojo.User;
import org.springframework.data.domain.Page;

public interface UserService {

    public Page<User> listUser(int start, int size);

}
