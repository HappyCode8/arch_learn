package com.wyj.service.security;


import com.wyj.entity.AccountServiceClient;
import com.wyj.entity.AuthenticAccount;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticAccountService implements UserDetailsService {
    private final AccountServiceClient accountService;

    public AuthenticAccountService(AccountServiceClient accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthenticAccount(Optional.ofNullable(accountService.findByUsername(username)).orElseThrow(() -> new UsernameNotFoundException("用户" + username + "不存在")));
    }
}
