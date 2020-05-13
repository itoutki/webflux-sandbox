package com.example.web.ec.service;

import com.example.web.ec.model.Account;
import com.example.web.ec.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findOne(String email) {
        return accountRepository.findByEmail(email).orElseThrow();
    }

    public void update(Account account) {
        accountRepository.updateById(account);
    }

    public void create(Account account, String password) {
        account.setEncodedPassword(password);
        accountRepository.create(account);
    }

}
