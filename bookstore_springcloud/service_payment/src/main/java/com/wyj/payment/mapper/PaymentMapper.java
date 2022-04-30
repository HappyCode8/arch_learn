package com.wyj.payment.mapper;

import com.wyj.payment.entity.Payment;
import com.wyj.payment.entity.Payment.State;
import org.apache.ibatis.annotations.Param;

/**
 * @author wyj
 * @description 针对表【payment】的数据库操作Mapper
 * @createDate 2022-04-10 17:21:48
 * @Entity com.wyj.domain.Payment
 */
public interface PaymentMapper {
    Payment selectOneById(@Param("id") Long id);

    int insertSelective(Payment payment);

    Payment selectOneByPayId(@Param("payId") String payId);

    int updatePayStateByPayId(@Param("payId") String payId, @Param("payState") State payState);
}




