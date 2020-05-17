package com.ilqjx.service.impl;

import com.ilqjx.dao.UserRepository;
import com.ilqjx.pojo.User;
import com.ilqjx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUser(User user) {
        return userRepository.getUserByNameAndPassword(user.getName(), user.getPassword());
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public Page<User> listUser(int start, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);
        Page<User> page = userRepository.findAll(pageable);
        return page;
    }

    @Override
    public boolean isExist(String name) {
        User user = getUserByName(name);
        return null != user;
    }

}
