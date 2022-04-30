package com.wyj.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName stockpile
 */
@Data
public class Stockpile implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Integer amount;

    /**
     * 
     */
    private Integer frozen;

    /**
     * product外键
     */
    private Long productId;

    private static final long serialVersionUID = 1L;

}