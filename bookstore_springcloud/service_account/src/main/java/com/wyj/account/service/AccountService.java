package com.wyj.account.service;

import com.wyj.account.mapper.AccountMapper;
import com.wyj.entity.Account;

import com.wyj.util.EncryptionUtil;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountMapper accountMapper;
    private final EncryptionUtil encoder;

    public AccountService(AccountMapper accountMapper, EncryptionUtil encoder) {
        this.accountMapper = accountMapper;
        this.encoder = encoder;
    }

    public Account findAccountByUsername(String username){
        return accountMapper.selectOneByUsername(username);
    }

    public void createAccount(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
        accountMapper.insertSelective(account);
    }

    public void updateAccount(Account account) {
        accountMapper.updateSelective(account);
    }
}
