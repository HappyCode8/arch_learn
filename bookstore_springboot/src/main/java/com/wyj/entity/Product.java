package com.wyj.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName product
 */
@Data
public class Product implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 书名
     */
    private String title;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 打折
     */
    private Double rate;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面
     */
    private String cover;

    /**
     * 
     */
    private String detail;

    private Boolean isVisible;

    private static final long serialVersionUID = 1L;
}