package com.wyj.payment.client;

import com.wyj.entity.DeliveredStatus;
import com.wyj.entity.Product;
import com.wyj.entity.Settlement;
import com.wyj.entity.Stockpile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 仓库商品和库存相关远程服务
 *
 * @author icyfenix@gmail.com
 * @date 2020/4/19 22:22
 **/
@FeignClient(name = "warehouse")
public interface ProductServiceClient {

    default void replenishProductInformation(Settlement bill) {
        bill.productMap = Stream.of(getProducts()).collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    @GetMapping("/restful-api/products/{id}")
    Product getProduct(@PathVariable("id") Integer id);

    @GetMapping("/restful-api/products")
    Product[] getProducts();

    default void decrease(Long productId, Integer amount) {
        setDeliveredStatus(productId, DeliveredStatus.DECREASE, amount);
    }

    default void increase(Long productId, Integer amount) {
        setDeliveredStatus(productId, DeliveredStatus.INCREASE, amount);
    }

    default void frozen(Long productId, Integer amount) {
        setDeliveredStatus(productId, DeliveredStatus.FROZEN, amount);
    }

    default void thawed(Long productId, Integer amount) {
        setDeliveredStatus(productId, DeliveredStatus.THAWED, amount);
    }

    @PatchMapping("/restful-api/products/stockpile/delivered/{productId}")
    void setDeliveredStatus(@PathVariable("productId") Long productId, @RequestParam("status") DeliveredStatus status, @RequestParam("amount") Integer amount);

    @GetMapping("/restful-api/products/stockpile/{productId}")
    Stockpile queryStockpile(@PathVariable("productId") Integer productId);
}
