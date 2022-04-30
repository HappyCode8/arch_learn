package com.wyj.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName specification
 */
@Data
public class Specification implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 条目
     */
    private String item;

    /**
     * 值
     */
    private String value;

    /**
     * product外键
     */
    private Long productId;

    private static final long serialVersionUID = 1L;
}