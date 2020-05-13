package com.example.web.ec.service;

import com.example.web.ec.model.Account;
import com.example.web.ec.model.Cart;
import com.example.web.ec.model.Order;

public class OrderService {
    public Order purchase(Account account, Cart cart, String signature) {
        if (cart.isEmpty()) {
            throw new RuntimeException("");
        }

        return null;
    }
}
