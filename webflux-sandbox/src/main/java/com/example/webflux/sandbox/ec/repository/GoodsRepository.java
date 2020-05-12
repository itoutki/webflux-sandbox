package com.example.webflux.sandbox.ec.repository;

import com.example.webflux.sandbox.ec.model.Goods;

import java.util.List;
import java.util.Optional;

public interface GoodsRepository {
    List<Goods> findByCategoryId(int categoryId);
    long countByCategoryId(int categoryId);
    Optional<Goods> findById(String id);
}
