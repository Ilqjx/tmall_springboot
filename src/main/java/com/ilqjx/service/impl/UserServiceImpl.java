package com.ilqjx.service.impl;

import java.util.Optional;

import com.ilqjx.dao.UserRepository;
import com.ilqjx.pojo.User;
import com.ilqjx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @CacheEvict(allEntries = true)
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Cacheable(key = "'users-one-user-' + #p0.id")
    public User getUser(User user) {
        return userRepository.getUserByNameAndPassword(user.getName(), user.getPassword());
    }

    @Override
    @Cacheable(key = "'users-one-' + #id")
    public User getUser(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        try {
            userOptional.get();
        } catch (Exception e) {
            return null;
        }
        return userOptional.get();
    }

    @Override
    @Cacheable(key = "'users-one-name-' + #p0")
    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    @Cacheable(key = "'users-page-' + #p0 + '-' + #p1")
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
