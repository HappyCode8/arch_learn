package com.wyj.controller;

import com.wyj.annotation.AuthenticatedAccount;
import com.wyj.entity.Account;
import com.wyj.model.web.Result;
import com.wyj.service.AccountService;
import com.wyj.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restful-api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{username}")
    public Account getUser(@PathVariable("username") String username) {
        return accountService.findAccountByUsername(username);
    }

    @PostMapping
    public Result createUser(@RequestBody Account user) {
       accountService.createAccount(user);
       return ResultUtil.ok();
    }

    @PutMapping
    public Result updateUser(@RequestBody @AuthenticatedAccount Account user) {
        accountService.updateAccount(user);
        return ResultUtil.ok();
    }
}
