package com.wyj.mapper;
import org.apache.ibatis.annotations.Param;
import com.wyj.entity.Stockpile;

/**
* @author wyj
* @description 针对表【stockpile】的数据库操作Mapper
* @createDate 2022-04-10 17:21:48
* @Entity com.wyj.domain.Stockpile
*/
public interface StockpileMapper {
    Stockpile selectOneByProductId(@Param("productId") Long productId);

    int updateAmountByProductId(@Param("productId") Long productId, @Param("amount") Integer amount);

    int updateFrozenByProductId(@Param("productId") Long productId, @Param("frozen") Integer frozen);

    int updateAmountAndFrozenByProductId(@Param("productId") Long productId, @Param("amount") Integer amount, @Param("frozen") Integer frozen);
}




