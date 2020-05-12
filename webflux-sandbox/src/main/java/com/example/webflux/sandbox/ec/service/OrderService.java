package com.example.webflux.sandbox.ec.service;

import com.example.webflux.sandbox.ec.model.Account;
import com.example.webflux.sandbox.ec.model.Cart;
import com.example.webflux.sandbox.ec.model.Order;

public class OrderService {
    public Order purchase(Account account, Cart cart, String signature) {
        if (cart.isEmpty()) {
            throw new RuntimeException("");
        }

        return null;
    }
}
