package com.wyj.account.controller;

import com.wyj.account.service.AccountService;
import com.wyj.entity.Account;
import com.wyj.model.web.Result;
import com.wyj.util.ResultUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restful-api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{username}")
    @PreAuthorize("#oauth2.hasAnyScope('SERVICE','BROWSER')")
    public Account getUser(@PathVariable("username") String username) {
        return accountService.findAccountByUsername(username);
    }

    @PostMapping
    public Result createUser(@RequestBody Account user) {
        accountService.createAccount(user);
        return ResultUtil.ok();
    }

    @PutMapping
    @PreAuthorize("#oauth2.hasAnyScope('BROWSER')")
    public Result updateUser(@RequestBody Account user) {
        accountService.updateAccount(user);
        return ResultUtil.ok();
    }
}