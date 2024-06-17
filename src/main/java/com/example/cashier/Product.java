package com.example.cashier;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String name;
    private Map<String, Double> sizePriceMap;
    private int quantity;
    private String imageUrl;
    private String size;
    private String sugar;
    private String ice;
    private String toppings;
    private String category;

    public Product(String name, double price, String imageUrl, String[] availableSizes, String category) {
        this.name = name;
        this.sizePriceMap = new HashMap<>();
        this.sizePriceMap.put(availableSizes[0], price);
        this.quantity = 1;
        this.imageUrl = imageUrl;
        this.size = availableSizes[0];
        this.sugar = "Normal";
        this.ice = "Normal";
        this.toppings = "";
        this.category = category;
    }

    public Product(String name, double smallPrice, double largePrice, String imageUrl, String[] availableSizes, String category) {
        this.name = name;
        this.sizePriceMap = new HashMap<>();
        this.sizePriceMap.put("Small", smallPrice);
        this.sizePriceMap.put("Large", largePrice);
        this.quantity = 1;
        this.imageUrl = imageUrl;
        this.size = availableSizes[0];
        this.sugar = "Normal";
        this.ice = "Normal";
        this.toppings = "";
        this.category = category;
    }

    public Product(String name, double price, int quantity, String size, String sugar, String ice, String imageUrl, String toppings, String category) {
        this.name = name;
        this.sizePriceMap = new HashMap<>();
        this.sizePriceMap.put(size, price);
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.size = size;
        this.sugar = sugar;
        this.ice = ice;
        this.toppings = toppings;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice(String size) {
        return sizePriceMap.get(size);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String[] getAvailableSizes() {
        return sizePriceMap.keySet().toArray(new String[0]);
    }

    public double getTotalPrice() {
        return getPrice(size) * quantity;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s sugar, %s ice, Toppings: %s) - %d x Rp%.2f = Rp%.2f",
                name, size, sugar, ice, toppings.isEmpty() ? "None" : toppings, quantity, getPrice(size), getTotalPrice());
    }
}
