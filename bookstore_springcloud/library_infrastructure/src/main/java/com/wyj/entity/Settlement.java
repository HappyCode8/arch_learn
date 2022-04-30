package com.wyj.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collection;
import java.util.Map;

@Data
public class Settlement {
    private Collection<Item> items;//物品的信息
    private Purchase purchase;//购物者的信息
    public transient Map<Long, Product> productMap;

    @Data
    public static class Item {
        private Integer amount;
        @JsonProperty("id")
        private Long productId;
    }

    @Data
    public static class Purchase {
        private Boolean delivery = true;
        private String pay;
        private String name;
        private String telephone;
        private String location;
    }
}
