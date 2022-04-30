package com.wyj.warehouse.controller;

import com.wyj.warehouse.entity.Advertisement;
import com.wyj.warehouse.service.AdvertisementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 广告相关的资源
 **/
@RestController
@RequestMapping("/restful-api/advertisements")
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping
    public List<Advertisement> getAllAdvertisements() {
        return advertisementService.findAll();
    }
}
