package com.wyj.payment.service;

import com.wyj.configuration.CacheConfiguration;
import com.wyj.entity.Settlement;
import com.wyj.payment.client.ProductServiceClient;
import com.wyj.payment.entity.Payment;
import com.wyj.payment.mapper.PaymentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@Service
@Slf4j
public class PaymentService {
    /**
     * 默认支付单超时时间：2分钟（缓存TTL时间的一半）
     */
    private static final long DEFAULT_PRODUCT_FROZEN_EXPIRES = CacheConfiguration.SYSTEM_DEFAULT_EXPIRES / 2;
    @Resource(name = "settlement")
    private Cache settlementCache;
    private final Timer timer = new Timer();

    private final ProductServiceClient stockpileService;
    private final PaymentMapper paymentMapper;

    public PaymentService(ProductServiceClient stockpileService, PaymentMapper paymentMapper) {
        this.stockpileService = stockpileService;
        this.paymentMapper = paymentMapper;
    }

    /**
     * 完成支付单
     * <p>
     * 意味着客户已经完成付款，这个方法在正式业务中应当作为三方支付平台的回调，而演示项目就直接由客户端发起调用了
     */
    public double accomplish(String payId) {
        synchronized (payId.intern()) {
            Payment payment = paymentMapper.selectOneByPayId(payId);
            if (payment.getPayState() == Payment.State.WAITING) {
                paymentMapper.updatePayStateByPayId(payId, Payment.State.PAYED);
                accomplishSettlement(Payment.State.PAYED, payment.getPayId());
                log.info("编号为{}的支付单已处理完成，等待支付", payId);
                return payment.getTotalPrice();
            } else {
                throw new UnsupportedOperationException("当前订单不允许支付，当前状态为：" + payment.getPayState());
            }
        }
    }

    /**
     * 取消支付单
     * <p>
     * 客户取消支付单，此时应当立即释放处于冻结状态的库存
     * 由于支付单的存储中应该保存而未持久化的购物明细（在Settlement中），所以这步就不做处理了，等2分钟后在触发器中释放
     */
    public void cancel(String payId) {
        synchronized (payId.intern()) {
            Payment payment = paymentMapper.selectOneByPayId(payId);
            if (payment.getPayState() == Payment.State.WAITING) {
                payment.setPayState(Payment.State.CANCEL);
                paymentMapper.insertSelective(payment);
                accomplishSettlement(Payment.State.CANCEL, payment.getPayId());
                log.info("编号为{}的支付单已被取消", payId);
            } else {
                throw new UnsupportedOperationException("当前订单不允许取消，当前状态为：" + payment.getPayState());
            }
        }
    }

    /**
     * 生成支付单
     * <p>
     * 根据结算单冻结指定的货物，计算总价，生成支付单
     */
    public Payment producePayment(Settlement bill) {
        Double total = bill.getItems().stream().mapToDouble(i -> {
            stockpileService.frozen(i.getProductId(), i.getAmount());
            return bill.productMap.get(i.getProductId()).getPrice() * i.getAmount();
        }).sum() + 12;   // 12元固定运费，客户端写死的，这里陪着演一下，避免总价对不上
        Payment payment = new Payment(total, DEFAULT_PRODUCT_FROZEN_EXPIRES);
        paymentMapper.insertSelective(payment);
        // 将支付单存入缓存
        settlementCache.put(payment.getPayId(), bill);
        log.info("创建支付订单，总额：{}", payment.getTotalPrice());
        return payment;
    }

    /**
     * 设置支付单自动冲销解冻的触发器
     * <p>
     * 如果在触发器超时之后，如果支付单未仍未被支付（状态是WAITING）
     * 则自动执行冲销，将冻结的库存商品解冻，以便其他人可以购买，并将Payment的状态修改为ROLLBACK。
     * <p>
     * 注意：
     * 使用TimerTask意味着节点带有状态，这在分布式应用中是必须明确【反对】的，如以下缺陷：
     * 1. 如果要考虑支付订单的取消场景，无论支付状态如何，这个TimerTask到时间之后都应当被执行。不应尝试使用TimerTask::cancel来取消任务。
     * 因为只有带有上下文状态的节点才能完成取消操作，如果要在集群中这样做，就必须使用支持集群的定时任务（如Quartz）以保证多节点下能够正常取消任务。
     * 2. 如果节点被重启、同样会面临到状态的丢失，导致一部分处于冻结的触发器永远无法被执行，所以需要系统启动时根据数据库状态有一个恢复TimeTask的的操作
     * 3. 即时只考虑正常支付的情况，真正生产环境中这种代码需要一个支持集群的同步锁（如用Redis实现互斥量），避免解冻支付和该支付单被完成两个事件同时在不同的节点中发生
     */
    public void setupAutoThawedTrigger(Payment payment) {
        timer.schedule(new TimerTask() {
            public void run() {
                synchronized (payment.getPayId().intern()) {
                    // 使用2分钟之前的Payment到数据库中查出当前的Payment
                    Payment currentPayment = paymentMapper.selectOneById(payment.getId());
                    if (currentPayment.getPayState() == Payment.State.WAITING) {
                        log.info("支付单{}当前状态为：WAITING，转变为：TIMEOUT", payment.getId());
                        accomplishSettlement(Payment.State.TIMEOUT, payment.getPayId());
                    }
                }
            }
        }, payment.getExpires());
    }

    /**
     * 根据支付状态，实际调整库存（扣减库存或者解冻）
     */
    private void accomplishSettlement(Payment.State endState, String payId) {
        Settlement settlement = (Settlement) Objects.requireNonNull(Objects.requireNonNull(settlementCache.get(payId)).get());
        settlement.getItems().forEach(i -> {
            if (endState == Payment.State.PAYED) {
                stockpileService.decrease(i.getProductId(), i.getAmount());//付款以后扣减库存
            } else {
                stockpileService.thawed(i.getProductId(), i.getAmount());// 其他状态，无论是TIMEOUT还是CANCEL，都进行解冻
            }
        });
    }
}