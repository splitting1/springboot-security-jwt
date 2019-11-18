package com.daisy.service.impl;

import com.daisy.common.factory.JwtUserFactory;
import com.daisy.entity.User;
import com.daisy.repository.UserRepository;
import com.daisy.service.JwtUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户名认证的Service类
 * @author wangyunpeng
 */
@Service
public class JwtUserDetailServiceImpl implements JwtUserDetailService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据用户名加载用户
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.",username));
        } else {
            return JwtUserFactory.create(user);
        }
    }
}
