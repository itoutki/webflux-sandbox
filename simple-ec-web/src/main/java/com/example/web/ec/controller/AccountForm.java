package com.example.web.ec.controller;

import com.example.web.ec.model.Account;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class AccountForm {
    private Account account;

    public AccountForm() {
    }

    public AccountForm(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
