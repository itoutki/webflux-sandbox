package com.example.web.ec.controller;

import com.example.web.ec.model.Goods;

import java.io.Serializable;

public class GoodsForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private int price;
    private int quantity;


    public GoodsForm() {
    }

    public GoodsForm(String id, String name, String description, int price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public GoodsForm(Goods goods) {
        this.id = goods.getId();
        this.name = goods.getName();
        this.description = goods.getDescription();
        this.price = goods.getPrice();
        this.quantity = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
