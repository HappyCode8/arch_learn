package com.wyj.warehouse.service;

import com.wyj.warehouse.entity.Advertisement;
import com.wyj.warehouse.mapper.AdvertisementMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {
    private final AdvertisementMapper advertisementMapper;

    public AdvertisementService(AdvertisementMapper advertisementMapper) {
        this.advertisementMapper = advertisementMapper;
    }

    public List<Advertisement> findAll(){
        return advertisementMapper.selectAll();
    }
}
