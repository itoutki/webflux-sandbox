package com.example.web.ec.controller;

import com.example.web.ec.model.Account;
import com.example.web.ec.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ec/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping()
    public Account get(@RequestParam(name = "email", required = true) String email) {
        return accountService.findOne(email);
    }

    @PostMapping()
    public Account create(@RequestBody Account account) {
        accountService.create(account, "password");
        return account;
    }

    @PostMapping("/{id}")
    public Account update(@RequestBody Account account) {
        accountService.update(account);
        return account;
    }

}
