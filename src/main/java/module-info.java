module com.example.cashier {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cashier to javafx.fxml;
    exports com.example.cashier;
}