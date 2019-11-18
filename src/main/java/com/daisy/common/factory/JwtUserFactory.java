package com.daisy.common.factory;

import com.daisy.common.jwt.JwtUser;
import com.daisy.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户工厂类
 * @author wangyunpeng
 */
public final class JwtUserFactory {
    private JwtUserFactory() {

    }

    /**
     * 根据用户转化为JwtUser 对外暴露
     * @param user
     * @return JwtUser
     */
    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                mapToGrantedAuthorities(user.getRoles()),
                user.getLastPasswordResetDate()
                );
    }

    /**
     * 角色集合转化
     * @param authorities
     * @return
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities ) {
        return authorities.stream().
                map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
