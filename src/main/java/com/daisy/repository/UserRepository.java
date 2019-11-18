package com.daisy.repository;

import com.daisy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户Repository类
 * @author wangyunpeng
 */
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String username);
}
