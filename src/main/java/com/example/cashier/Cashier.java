package com.example.cashier;

import java.util.ArrayList;
import java.util.List;

public class Cashier {
    private List<Product> productList;

    public Cashier() {
        productList = new ArrayList<>();
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public void removeProduct(Product product) {
        productList.remove(product);
    }

    public double calculateTotal() {
        double total = 0;
        for (Product product : productList) {
            total += product.getTotalPrice();
        }
        return total;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void clearProducts() {
        productList.clear();
    }

    public String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        for (Product product : productList) {
            receipt.append(product.toString()).append("\n");
        }
        receipt.append("Total: ").append(calculateTotal()).append("\n");
        return receipt.toString();
    }
}
