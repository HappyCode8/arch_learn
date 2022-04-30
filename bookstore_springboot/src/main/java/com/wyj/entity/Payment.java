package com.wyj.entity;

import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 
 * @TableName payment
 */
@Data
public class Payment implements Serializable {
    /**
     * 支付状态
     */
    public enum State {
        /**
         * 等待支付中
         */
        WAITING,
        /**
         * 已取消
         */
        CANCEL,
        /**
         * 已支付
         */
        PAYED,
        /**
         * 已超时回滚（未支付，并且商品已恢复）
         */
        TIMEOUT
    }

    public Payment(Double totalPrice, Long expires) {
        setTotalPrice(totalPrice);
        setExpires(expires);
        setCreateTime(new Date());
        setPayState(State.WAITING);
        // 下面这两个是随便写的，实际应该根据情况调用支付服务，返回待支付的ID
        setPayId(UUID.randomUUID().toString());
        // 产生支付单的时候一定是有用户的
        Account account = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //前端付款完成以后的回调链接
        setPaymentLink("/pay/modify/" + getPayId() + "?state=PAYED&accountId=" + account.getId());
    }
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String payId;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Double totalPrice;

    /**
     * 
     */
    private Long expires;

    /**
     * 
     */
    private String paymentLink;

    /**
     * 
     */
    private State payState;

    private static final long serialVersionUID = 1L;
}