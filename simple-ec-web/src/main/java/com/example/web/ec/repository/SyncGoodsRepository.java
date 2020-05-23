package com.example.web.ec.repository;

import com.example.web.ec.model.Goods;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SyncGoodsRepository implements GoodsRepository {
    final Map<String, Goods> goodsList = new ConcurrentHashMap<>();

    public SyncGoodsRepository() {
        String id = "0";
        Goods goods = new Goods();
        goods.setId(id);
        goods.setCategoryId(0);
        goods.setName("コカコーラ");
        goods.setPrice(100);

        goodsList.put("0", new Goods("0", "コカコーラ", "コカコーラ", 0, 120));
        goodsList.put("1", new Goods("1", "レッドブル", "翼を授ける", 0, 190));
        goodsList.put("2", new Goods("2", "モンスター", "モンスター", 0, 190));
        goodsList.put("3", new Goods("3", "チキンタツタ", "期間限定", 1, 340));
        goodsList.put("4", new Goods("4", "ハンバーガー", "ハンバーガー", 1, 120));
    }

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
        return Optional.of(goodsList.get(id));
    }
}
