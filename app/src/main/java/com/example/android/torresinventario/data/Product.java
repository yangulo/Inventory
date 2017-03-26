package com.example.android.torresinventario.data;

import java.io.Serializable;

public class Product implements Serializable {
    private final int id;
    private final String name;
    private final String description;
    private final int price;
    private int stock;
    private final int quantityToPurchase;

    public Product(int id, String name, String description, int price, int stock, int quantityToPurchase) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.quantityToPurchase = quantityToPurchase;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getQuantityToPurchase() {
        return quantityToPurchase;
    }

    public void sale() {
        if (stock != 0) {
            stock = stock - 1;
        }
    }

    public void receive() {
        stock = stock + quantityToPurchase;
    }
}
