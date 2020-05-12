package com.example.webflux.sandbox.ec.repository;

import com.example.webflux.sandbox.ec.model.Order;
import com.example.webflux.sandbox.ec.model.OrderLine;

import java.util.List;

public interface OrderRepository {
    void createOrderLines(List<OrderLine> orderLines);
    void createOrder(Order order);
}
