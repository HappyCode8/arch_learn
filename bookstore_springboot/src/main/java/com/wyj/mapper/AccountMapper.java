package com.wyj.mapper;
import org.apache.ibatis.annotations.Param;

import com.wyj.entity.Account;

/**
* @author wyj
* @description 针对表【account】的数据库操作Mapper
* @createDate 2022-04-10 17:21:48
* @Entity com.wyj.domain.Account
*/
public interface AccountMapper {
    Account selectOneByUsername(@Param("username") String username);

    int insertSelective(Account account);

    int updateSelective(Account account);
}




