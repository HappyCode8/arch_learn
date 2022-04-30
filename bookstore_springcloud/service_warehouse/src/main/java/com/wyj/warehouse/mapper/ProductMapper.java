package com.wyj.warehouse.mapper;

import com.wyj.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author wyj
* @description 针对表【product】的数据库操作Mapper
* @createDate 2022-04-10 17:21:48
* @Entity com.wyj.domain.Product
*/
public interface ProductMapper {
    List<Product> selectAll();

    Product selectOneById(@Param("id") Long id);

    int updateIsVisibleById(@Param("isVisible") Boolean isVisible, @Param("id") Long id);

    int updateSelectiveById(Product product);

    int insertSelective(Product product);

    List<Product> selectAllByIdIn(@Param("idList") List<Long> idList);
}




