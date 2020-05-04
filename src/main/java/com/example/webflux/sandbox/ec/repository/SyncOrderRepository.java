package com.example.webflux.sandbox.ec.repository;

import com.example.webflux.sandbox.ec.model.Order;
import com.example.webflux.sandbox.ec.model.OrderLine;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SyncOrderRepository implements OrderRepository {
    final List<Order> orders = new CopyOnWriteArrayList<>();

    @Override
    public void createOrderLines(List<OrderLine> orderLines) {

    }

    @Override
    public void createOrder(Order order) {
        orders.add(order);
    }
}
