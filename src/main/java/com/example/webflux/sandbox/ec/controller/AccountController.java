package com.example.webflux.sandbox.ec.controller;

import com.example.webflux.sandbox.ec.model.Account;
import com.example.webflux.sandbox.ec.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ec")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts/{email}")
    public Account get(@PathVariable(name = "email") String email) {
        return accountService.findOne(email);
    }

    @PostMapping("/accounts/{email}")
    public Account update(@RequestBody Account account) {
        accountService.update(account);
        return account;
    }

    @PostMapping("/accounts")
    public Account create(@RequestBody Account account) {
        accountService.create(account, "password");
        return account;
    }
}
