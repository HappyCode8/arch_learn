package com.wyj.service.security;

import com.wyj.mapper.AccountMapper;
import com.wyj.entity.AuthenticAccount;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticAccountService implements UserDetailsService {
    private final AccountMapper accountMapper;

    public AuthenticAccountService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthenticAccount(Optional.ofNullable(accountMapper.selectOneByUsername(username)).orElseThrow(() -> new UsernameNotFoundException("用户" + username + "不存在")));
    }
}
