package com.example.web.ec.service;

import com.example.web.ec.model.Goods;
import com.example.web.ec.repository.GoodsRepository;

import java.util.Collections;
import java.util.List;

public class GoodsService {
    private GoodsRepository goodsRepository;

    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public List<Goods> findByCategoryId(int categoryId) {
        long total = goodsRepository.countByCategoryId(categoryId);
        List<Goods> goodsList = Collections.emptyList();
        if (total > 0) {
            goodsList = goodsRepository.findByCategoryId(categoryId);
        }

        return goodsList;
    }
}
