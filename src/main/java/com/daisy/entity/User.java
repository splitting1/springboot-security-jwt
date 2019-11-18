package com.daisy.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 * @date 2019-11-16
 * @author wangyunpeng
 */
@Data
@ToString
@Entity
@Table(name="sys_user")
public class User {
    @Id
    /**
     * 主键的生成策略
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private Integer status;
    /**
     * 忽略字段的映射
     */
    @Transient
    private String email;
    @Transient
    private Date lastPasswordResetDate;
    @Transient
    private List<String> roles;
}
