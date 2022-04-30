package com.wyj.service;

import com.wyj.entity.Stockpile;
import com.wyj.mapper.StockpileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockpileService {
    private final StockpileMapper stockpileMapper;

    public StockpileService(StockpileMapper stockpileMapper) {
        this.stockpileMapper = stockpileMapper;
    }

    public Stockpile getStockpile(Long productId) {
        return stockpileMapper.selectOneByProductId(productId);
    }

    public void setStockpileAmountByProductId(Long productId, Integer amount) {
        stockpileMapper.updateAmountByProductId(productId, amount);
    }

    /**
     * 货物冻结
     * 从正常货物中移动指定数量至冻结状态
     */
    public void frozen(Long productId, Integer amount) {
        Stockpile stock = stockpileMapper.selectOneByProductId(productId);
        stockpileMapper.updateAmountAndFrozenByProductId(productId, stock.getAmount()-amount, stock.getFrozen()+amount);
        log.info("冻结库存，商品：{}，数量：{}", productId, amount);
    }

    /**
     * 货物售出, 从冻结状态的货物中扣减
     */
    public void decrease(Long productId, Integer amount) {
        Stockpile stock = stockpileMapper.selectOneByProductId(productId);
        stockpileMapper.updateFrozenByProductId(productId, stock.getFrozen()-amount);
        log.info("库存出库，商品：{}，数量：{}", productId, amount);
    }

    /**
     * 货物解冻
     * 从冻结货物中移动指定数量至正常状态
     */
    public void thawed(Long productId, Integer amount) {
        Stockpile stock = stockpileMapper.selectOneByProductId(productId);
        stockpileMapper.updateAmountAndFrozenByProductId(productId, stock.getAmount()+amount,stock.getFrozen()-amount);
        log.info("解冻库存，商品：{}，数量：{}", productId, amount);
    }
}
