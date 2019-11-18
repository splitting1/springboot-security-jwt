package com.daisy.controller;

import com.daisy.entity.User;
import com.daisy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户处理器
 * @author wangyunpeng
 * @PreAuthorize 该注解中我们可以使用内建的 SPEL 表达式;比如hasRole() 来决定哪些用户有权访问
 */
@RestController
@RequestMapping(value = "/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

}
