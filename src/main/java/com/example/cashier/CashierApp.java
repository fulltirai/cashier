package com.example.cashier;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.stream.Collectors;

public class CashierApp extends Application {

    private Cashier cashier = new Cashier();
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ObservableList<Product> cashierProducts = FXCollections.observableArrayList();
    private ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    private Stage primaryStage;

    private Label totalPriceLabel;
    private TextField paymentField;
    private Label changeLabel;

    private static final String IMAGE_PATH = "file:src/main/resources/com/example/cashier/images/";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Cashier Application");

        products.addAll(
                new Product("Chizu Matcha", 19000, IMAGE_PATH + "chizu_matcha.png", new String[]{"Medium"}, "Non-Tea"),
                new Product("Cokolateh", 17000, IMAGE_PATH + "cokolateh.png", new String[]{"Medium"}, "Non-Tea"),
                new Product("Esteh Susu Nusantara", 8000, 14000, IMAGE_PATH + "esteh_susu_nusantara.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Esteh Mango Greentea", 17000, IMAGE_PATH + "esteh_mango_greentea.png", new String[]{"Medium"}, "Tea"),
                new Product("Esteh Lemonade Berry", 16000, IMAGE_PATH + "esteh_lemonade_berry.png", new String[]{"Medium"}, "Tea"),
                new Product("Chizu Red Velvet", 19000, IMAGE_PATH + "chizu_red_velvet.png", new String[]{"Medium"}, "Non-Tea"),
                new Product("Chizu Avocado", 19000, IMAGE_PATH + "chizu_avocado.png", new String[]{"Medium"}, "Non-Tea"),
                new Product("Chizu Taro", 19000, IMAGE_PATH + "chizu_taro.png", new String[]{"Medium"}, "Non-Tea"),
                new Product("Esteh Susu Nusaberry", 16000, IMAGE_PATH + "esteh_susu_nusaberry.png", new String[]{"Medium"}, "Tea"),
                new Product("Esteh Lemon", 9000, 14000, IMAGE_PATH + "esteh_lemon.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Esteh Matcha", 13000, 17000, IMAGE_PATH + "esteh_matcha.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Esteh Red Velvet", 12000, 17000, IMAGE_PATH + "esteh_red_velvet.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Esteh Avocado", 12000, 17000, IMAGE_PATH + "esteh_avocado.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Esteh Leci", 9000, 14000, IMAGE_PATH + "esteh_leci.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Esteh Hijau Original", 8000, 12000, IMAGE_PATH + "esteh_hijau_original.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Esteh Taro", 12000, 17000, IMAGE_PATH + "esteh_taro.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Brown Sugar", 19000, IMAGE_PATH + "brown_sugar.png", new String[]{"Medium"}, "Non-Tea"),
                new Product("Sea Salt Cookies & Cream", 19000, IMAGE_PATH + "sea_salt_cookies_cream.png", new String[]{"Medium"}, "Non-Tea"),
                new Product("Esteh Melati", 6000, 9000, IMAGE_PATH + "esteh_melati.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Esteh Original", 6000, 9000, IMAGE_PATH + "esteh_original.png", new String[]{"Medium", "Large"}, "Tea"),
                new Product("Milo", 17000, IMAGE_PATH + "milo.png", new String[]{"Medium"}, "Non-Tea"),
                new Product("Thai Tea", 7000, 11000, IMAGE_PATH + "thai_tea.png", new String[]{"Medium", "Large"}, "Tea")
        );

        filteredProducts.addAll(products);

        ComboBox<String> categoryFilterComboBox = new ComboBox<>();
        categoryFilterComboBox.getItems().addAll("All", "Tea", "Non-Tea");
        categoryFilterComboBox.setValue("All");
        categoryFilterComboBox.setOnAction(e -> filterProducts(categoryFilterComboBox.getValue()));

        TilePane imageGrid = createImageGrid();

        ScrollPane scrollPane = new ScrollPane(imageGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefViewportHeight(600);

        ListView<Product> productListView = new ListView<>(cashierProducts);
        productListView.setPrefHeight(300);
        productListView.setPrefWidth(320);

        VBox productListViewBox = new VBox(10, new Label("Cart"), productListView);

        VBox receiptBox = new VBox(10, productListViewBox);
        receiptBox.setPadding(new Insets(10));

        VBox filterBox = new VBox(10, new Label("Filter by Category"), categoryFilterComboBox);

        // Add search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search for a product");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchProducts(newValue));

        VBox searchBox = new VBox(10, new Label("Search"), searchField);

        totalPriceLabel = new Label("Total: " + formatPrice(0));
        paymentField = new TextField();
        paymentField.setPromptText("Enter amount paid");

        // Add numeric filter to payment field
        addNumericFilter(paymentField);

        Button payButton = new Button("Pay");
        payButton.setOnAction(e -> processPayment());

        changeLabel = new Label("Change: " + formatPrice(0));

        VBox paymentBox = new VBox(10, new Label("Payment"), totalPriceLabel, paymentField, payButton, changeLabel);
        paymentBox.setPadding(new Insets(10));

        VBox cartAndPaymentBox = new VBox(20, receiptBox, paymentBox);

        VBox filterAndSearchBox = new VBox(20, filterBox, searchBox);

        HBox mainLayout = new HBox(10, filterAndSearchBox, scrollPane, cartAndPaymentBox);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        HBox.setHgrow(cartAndPaymentBox, Priority.NEVER);

        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void addNumericFilter(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private TilePane createImageGrid() {
        TilePane imageGrid = new TilePane();
        imageGrid.setPadding(new Insets(10, 10, 10, 10));
        imageGrid.setVgap(8);
        imageGrid.setHgap(10);
        imageGrid.setPrefColumns(4);

        for (Product product : filteredProducts) {
            VBox productBox = new VBox(5);
            ImageView imageView = new ImageView(new Image(product.getImageUrl()));
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            Label nameLabel = new Label(product.getName());
            Label priceLabel = new Label(formatPrice(product.getPrice(product.getAvailableSizes()[0])));
            Button addButton = new Button("Add to Cart");

            addButton.setOnAction(e -> showAddProductDialog(product));

            productBox.getChildren().addAll(imageView, nameLabel, priceLabel, addButton);
            imageGrid.getChildren().add(productBox);
        }

        return imageGrid;
    }

    private void filterProducts(String category) {
        filteredProducts.clear();
        if (category.equals("All")) {
            filteredProducts.addAll(products);
        } else {
            filteredProducts.addAll(products.stream()
                    .filter(product -> product.getCategory().equals(category))
                    .collect(Collectors.toList()));
        }
        refreshImageGrid();
    }

    private void searchProducts(String searchQuery) {
        filteredProducts.clear();
        if (searchQuery.isEmpty()) {
            filteredProducts.addAll(products);
        } else {
            filteredProducts.addAll(products.stream()
                    .filter(product -> product.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        refreshImageGrid();
    }

    private void refreshImageGrid() {
        ScrollPane scrollPane = (ScrollPane) primaryStage.getScene().getRoot().lookup(".scroll-pane");
        TilePane imageGrid = createImageGrid();
        scrollPane.setContent(imageGrid);
    }

    private void showAddProductDialog(Product product) {
        Dialog<ProductDetails> dialog = new Dialog<>();
        dialog.setTitle("Add Product");

        Label quantityLabel = new Label("Quantity:");
        TextField quantityField = new TextField();
        quantityField.setText("1");

        Label sizeLabel = new Label("Size:");
        ComboBox<String> sizeComboBox = new ComboBox<>();
        sizeComboBox.getItems().addAll(product.getAvailableSizes());
        sizeComboBox.setValue(product.getAvailableSizes()[0]);

        Label sugarLabel = new Label("Sugar Level:");
        ComboBox<String> sugarComboBox = new ComboBox<>();
        sugarComboBox.getItems().addAll("Normal", "Less", "Zero");
        sugarComboBox.setValue("Normal");

        Label iceLabel = new Label("Ice Level:");
        ComboBox<String> iceComboBox = new ComboBox<>();
        iceComboBox.getItems().addAll("Normal", "Less", "Zero");
        iceComboBox.setValue("Normal");

        Label toppingsLabel = new Label("Toppings:");
        CheckBox cincauCheckbox = new CheckBox("Cincau");
        CheckBox krimSeaSaltCheckbox = new CheckBox("Krim Sea Salt");
        CheckBox krimKejuCheckbox = new CheckBox("Krim Keju");
        CheckBox pudingSusuCheckbox = new CheckBox("Puding Susu");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(quantityLabel, 0, 0);
        grid.add(quantityField, 1, 0);
        grid.add(sizeLabel, 0, 1);
        grid.add(sizeComboBox, 1, 1);
        grid.add(sugarLabel, 0, 2);
        grid.add(sugarComboBox, 1, 2);
        grid.add(iceLabel, 0, 3);
        grid.add(iceComboBox, 1, 3);
        grid.add(toppingsLabel, 0, 4);
        grid.add(cincauCheckbox, 1, 4);
        grid.add(krimSeaSaltCheckbox, 1, 5);
        grid.add(krimKejuCheckbox, 1, 6);
        grid.add(pudingSusuCheckbox, 1, 7);

        dialog.getDialogPane().setContent(grid);

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                int quantity = Integer.parseInt(quantityField.getText());
                String size = sizeComboBox.getValue();
                String sugar = sugarComboBox.getValue();
                String ice = iceComboBox.getValue();
                double additionalCost = 0;
                StringBuilder toppings = new StringBuilder();

                if (cincauCheckbox.isSelected()) {
                    additionalCost += 4000;
                    toppings.append("Cincau, ");
                }
                if (krimSeaSaltCheckbox.isSelected()) {
                    additionalCost += 4000;
                    toppings.append("Krim Sea Salt, ");
                }
                if (krimKejuCheckbox.isSelected()) {
                    additionalCost += 4000;
                    toppings.append("Krim Keju, ");
                }
                if (pudingSusuCheckbox.isSelected()) {
                    additionalCost += 4000;
                    toppings.append("Puding Susu, ");
                }

                if (toppings.length() > 0) {
                    toppings.setLength(toppings.length() - 2);
                }

                double finalPrice = product.getPrice(size) + additionalCost;

                return new ProductDetails(product.getName(), finalPrice, quantity, size, sugar, ice, product.getImageUrl(), toppings.toString());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(details -> {
            Product newProduct = new Product(details.getName(), details.getPrice(), details.getQuantity(), details.getSize(), details.getSugar(), details.getIce(), details.getImageUrl(), details.getToppings(), product.getCategory());
            cashier.addProduct(newProduct);
            cashierProducts.add(newProduct);
            updateTotalPrice();
        });
    }

    private void updateTotalPrice() {
        double total = cashier.calculateTotal();
        totalPriceLabel.setText("Total: " + formatPrice(total));
    }

    private String formatPrice(double price) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return currencyFormatter.format(price);
    }

    private void printReceipt() {
        double total = cashier.calculateTotal();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Receipt");
        alert.setHeaderText("Total: " + formatPrice(total));
        alert.setContentText(cashier.generateReceipt() + "\nPayment: " + formatPrice(Double.parseDouble(paymentField.getText())) +
                "\nChange: " + changeLabel.getText());
        alert.showAndWait();
    }

    private void clearCart() {
        cashier.clearProducts();
        cashierProducts.clear();
    }

    private void processPayment() {
        try {
            double total = cashier.calculateTotal();
            double payment = Double.parseDouble(paymentField.getText());
            if (payment < total) {
                showAlert(Alert.AlertType.ERROR, "Insufficient Payment", "Payment amount is less than total.");
                return;
            }

            double change = payment - total;
            changeLabel.setText("Change: " + formatPrice(change));

            printReceipt();

            clearCart();

            paymentField.clear();
            changeLabel.setText("Change: " + formatPrice(0));
            totalPriceLabel.setText("Total: " + formatPrice(0));

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Payment", "Please enter a valid numeric amount.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
