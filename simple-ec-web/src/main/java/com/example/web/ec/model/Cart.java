package com.example.web.ec.model;

import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, CartItem> cartItems = new LinkedHashMap<>();

    public Collection<CartItem> getCartItems() {
        return cartItems.values();
    }

    public Cart add(CartItem cartItem) {
        String goodsId = cartItem.getGoods().getId();
        int nowQuantity = 0;
        CartItem cartItemInCart = cartItems.get(goodsId);
        if (cartItemInCart != null) {
            nowQuantity = cartItemInCart.getQuantity();
        }

        int totalQuantity = cartItem.getQuantity() + nowQuantity;
        cartItem.setQuantity(totalQuantity);
        cartItems.put(goodsId, cartItem);

        return this;
    }

    public Cart clear() {
        cartItems.clear();
        return this;
    }

    public Cart remove(Set<String> removedItemsIds) {
        for (String key : removedItemsIds) {
            cartItems.remove(key);
        }

        return this;
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    public int getTotalAmount() {
        int amount = 0;
        for (CartItem cartItem : cartItems.values()) {
            amount += cartItem.getGoods().getPrice() * cartItem.getQuantity();
        }

        return amount;
    }

    public String calcSignature() {
        byte[] serialized = SerializationUtils.serialize(this);
        byte[] signature = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            signature = messageDigest.digest(serialized);
        } catch (NoSuchAlgorithmException e) {
        }
        return Base64.getEncoder().encodeToString(signature);
    }
}
