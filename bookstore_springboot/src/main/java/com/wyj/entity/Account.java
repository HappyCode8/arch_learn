package com.wyj.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName account
 */
@Data
public class Account implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 名字
     */
    private String name;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 位置
     */
    private String location;

    private static final long serialVersionUID = 1L;
}