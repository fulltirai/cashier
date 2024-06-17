package com.example.cashier;

public class ProductDetails {

    private String name;
    private double price;
    private int quantity;
    private String size;
    private String sugar;
    private String ice;
    private String imageUrl;
    private String toppings;

    public ProductDetails(String name, double price, int quantity, String size, String sugar, String ice, String imageUrl, String toppings) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.sugar = sugar;
        this.ice = ice;
        this.imageUrl = imageUrl;
        this.toppings = toppings;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }

    public String getSugar() {
        return sugar;
    }

    public String getIce() {
        return ice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getToppings() {
        return toppings;
    }
}
