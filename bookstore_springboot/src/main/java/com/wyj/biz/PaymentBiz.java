package com.wyj.biz;

import com.wyj.entity.Settlement;
import com.wyj.entity.Payment;
import com.wyj.service.PaymentService;
import com.wyj.service.ProductService;
import com.wyj.service.WalletService;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentBiz {

    @Resource(name = "settlement")
    private Cache settlementCache;

    private final PaymentService paymentService;
    private final ProductService productService;
    private final WalletService walletService;

    public PaymentBiz(PaymentService paymentService, ProductService productService, WalletService walletService) {
        this.paymentService = paymentService;
        this.productService = productService;
        this.walletService = walletService;
    }

    /**
     * 根据结算清单的内容执行，生成对应的支付单
     */
    public Payment executeBySettlement(Settlement bill) {
        // 从服务中获取商品的价格，计算要支付的总价（安全原因，这个不能由客户端传上来）
        productService.replenishProductInformation(bill);
        // 冻结部分库存（保证有货提供）,生成付款单
        Payment payment = paymentService.producePayment(bill);
        // 设立解冻定时器（超时未支付则释放冻结的库存和资金）
        paymentService.setupAutoThawedTrigger(payment);
        return payment;
    }

    /**
     * 完成支付
     * 立即取消解冻定时器，执行扣减库存和资金
     */
    public void accomplishPayment(Long accountId, String payId) {
        // 订单从冻结状态变为派送状态，扣减库存
        double price = paymentService.accomplish(payId);
        // 扣减货款
        walletService.decrease(accountId, price);
        // 支付成功的清除缓存
        settlementCache.evict(payId);
    }

    /**
     * 取消支付
     * 立即触发解冻定时器，释放库存和资金
     */
    public void cancelPayment(String payId) {
        // 释放冻结的库存
        paymentService.cancel(payId);
        // 支付成功的清除缓存
        settlementCache.evict(payId);
    }
}
