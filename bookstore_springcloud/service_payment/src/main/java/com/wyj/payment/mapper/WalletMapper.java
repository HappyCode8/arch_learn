package com.wyj.payment.mapper;

import com.wyj.payment.entity.Wallet;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
* @author wyj
* @description 针对表【wallet】的数据库操作Mapper
* @createDate 2022-04-10 17:21:48
* @Entity com.wyj.domain.Wallet
*/
public interface WalletMapper {
    Optional<Wallet> selectOneByAccountId(@Param("accountId") Long accountId);

    int insertSelective(Wallet wallet);

    int updateMoneyByAccountId(@Param("accountId") Long accountId,@Param("money") Double money);
}




