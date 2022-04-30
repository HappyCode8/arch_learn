package com.wyj.entity;

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
    private Long productId;

    private static final long serialVersionUID = 1L;
}