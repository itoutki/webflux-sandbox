package com.example.webflux.sandbox.ec.repository;

import com.example.webflux.sandbox.ec.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findByEmail(String email);
    void create(Account account);
    boolean updateById(Account account);
}
