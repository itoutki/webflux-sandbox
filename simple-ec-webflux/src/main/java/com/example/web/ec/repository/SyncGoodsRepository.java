package com.example.web.ec.repository;

import com.example.web.ec.model.Goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SyncGoodsRepository implements GoodsRepository {
    final Map<String, Goods> goodsList = new ConcurrentHashMap<>();

    @Override
    public List<Goods> findByCategoryId(int categoryId) {
        List<Goods> results = new ArrayList<>();
        for(Goods goods : goodsList.values()) {
            if (goods.getCategoryId() == categoryId) {
                results.add(goods);
            }
        }
        return results;
    }

    @Override
    public long countByCategoryId(int categoryId) {
        return findByCategoryId(categoryId).size();
    }

    @Override
    public Optional<Goods> findById(String id) {
        return Optional.ofNullable(goodsList.get(id));
    }
}
