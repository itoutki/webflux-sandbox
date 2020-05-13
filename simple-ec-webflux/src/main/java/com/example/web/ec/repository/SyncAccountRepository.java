package com.example.web.ec.repository;

import com.example.web.ec.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class SyncAccountRepository implements AccountRepository {
    final Map<String, Account> accounts = new ConcurrentHashMap<>();
    final AtomicInteger sequence = new AtomicInteger(0);

    public SyncAccountRepository() {
        String email = "test01@example.com";
        int id = sequence.getAndIncrement();
        Account account = new Account();
        account.setId(String.valueOf(id));
        account.setEmail("test01@example.com");
        accounts.put(email, account);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return Optional.of(accounts.get(email));
    }

    @Override
    public void create(Account account) {
        int id = sequence.getAndIncrement();
        account.setId(String.valueOf(id));
        accounts.put(account.getEmail(), account);
    }

    @Override
    public boolean updateById(Account account) {
        if (!accounts.containsKey(account.getEmail())) return false;
        accounts.replace(account.getEmail(), account);
        return true;
    }
}
