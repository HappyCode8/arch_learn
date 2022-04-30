package com.wyj.warehouse.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName advertisement
 */
@Data
public class Advertisement implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String image;

    /**
     * 产品外键
     */
    private Integer productId;

    private static final long serialVersionUID = 1L;
}