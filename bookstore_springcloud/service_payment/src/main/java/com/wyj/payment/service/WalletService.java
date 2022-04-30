package com.wyj.payment.service;

import com.wyj.payment.entity.Wallet;
import com.wyj.payment.mapper.WalletMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WalletService {
    private final WalletMapper walletMapper;

    public WalletService(WalletMapper walletMapper) {
        this.walletMapper = walletMapper;
    }

    /**
     * 账户资金减少
     */
    public void decrease(Long accountId, Double amount) {
        Wallet wallet = walletMapper.selectOneByAccountId(accountId).orElseGet(() -> {
            Wallet newWallet = new Wallet();
            newWallet.setMoney(0D);
            walletMapper.insertSelective(newWallet);
            return newWallet;
        });
        if (wallet.getMoney() > amount) {
            walletMapper.updateMoneyByAccountId(accountId, wallet.getMoney() - amount);
            log.info("支付成功。用户余额：{}，本次消费：{}", wallet.getMoney(), amount);
        } else {
            throw new RuntimeException("用户余额不足以支付，请先充值");
        }
    }
}
