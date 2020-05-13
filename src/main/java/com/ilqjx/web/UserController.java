package com.ilqjx.web;

import com.ilqjx.pojo.User;
import com.ilqjx.service.UserService;
import com.ilqjx.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public PageUtil<User> listUser(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) {
        start = start < 0 ? 0 : start;
        int navigatePages = 5;
        Page<User> page = userService.listUser(start, size);
        PageUtil<User> pageUtil = new PageUtil<>(page, navigatePages);
        return pageUtil;
    }

}
