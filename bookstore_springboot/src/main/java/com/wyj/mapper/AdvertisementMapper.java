package com.wyj.mapper;
import java.util.List;

import com.wyj.entity.Advertisement;

/**
* @author wyj
* @description 针对表【advertisement】的数据库操作Mapper
* @createDate 2022-04-10 17:21:48
* @Entity com.wyj.domain.Advertisement
*/
public interface AdvertisementMapper {
    List<Advertisement> selectAll();
}




