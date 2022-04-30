package com.wyj.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName wallet
 */
@Data
public class Wallet implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 钱
     */
    private Double money;

    /**
     * 账号id,account外键
     */
    private Long accountId;

    private static final long serialVersionUID = 1L;
}