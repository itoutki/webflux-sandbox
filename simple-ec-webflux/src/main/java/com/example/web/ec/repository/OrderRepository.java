package com.example.web.ec.repository;

import com.example.web.ec.model.Order;
import com.example.web.ec.model.OrderLine;

import java.util.List;

public interface OrderRepository {
    void createOrderLines(List<OrderLine> orderLines);
    void createOrder(Order order);
}
