package C482TC.Model;

import javafx.beans.property.*;
import javafx.collections.*;

public class Products {

    private static ObservableList<Parts> parts = FXCollections.observableArrayList();
    private final IntegerProperty productID;
    private final StringProperty productName;
    private final DoubleProperty productPrice;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;

    public Products(){
        productID = new SimpleIntegerProperty();
        productName = new SimpleStringProperty();
        productPrice = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    // GET

    // ProductID
    public IntegerProperty productIDProperty() {
        return productID;
    }

    public int getProductID(){
        return this.productID.get();
    }

    // ProductName
    public StringProperty productNameProperty() {
        return productName;
    }

    public String getProductName(){
        return this.productName.get();
    }

    // ProductPrice
    public DoubleProperty productPriceProperty() {
        return productPrice;
    }

    public double getProductPrice(){
        return this.productPrice.get();
    }

    // InStock
    public IntegerProperty inStockProperty() {
        return inStock;
    }

    public int getInStock(){
        return this.inStock.get();
    }

    // Min
    public IntegerProperty minProperty() {
        return min;
    }

    public int getMin(){
        return this.min.get();
    }

    // Max
    public IntegerProperty maxProperty() {
        return max;
    }

    public int getMax(){
        return this.max.get();
    }

    public ObservableList getProductParts(){
        return parts;
    }

    // SET

    // ProductID
    public void setProductID(int productID){
        this.productID.set(productID);
    }

    // ProductName
    public void setProductName(String productName){
        this.productName.set(productName);
    }

    // ProductPrice
    public void setProductPrice(double productPrice){
        this.productPrice.set(productPrice);
    }

    // InStock
    public void setInStock(int inStock){
        this.inStock.set(inStock);
    }

    // Min
    public void setMin(int min){
        this.min.set(min);
    }

    // Max
    public void setMax(int max){
        this.max.set(max);
    }

    // Parts
    public void setProductParts(ObservableList<Parts> parts){
        this.parts = parts;
    }

    // ERROR HANDLING
    public static String isValid(String name, int min, int max, int inv, double price, ObservableList<Parts> parts, String error){
        double sumOfParts = 0.00;
        for (int i = 0; i < parts.size(); i++) {
            sumOfParts = sumOfParts + parts.get(i).getPartPrice();
        }

        if (name == null) {
            error = error + "Name is required.";
        }
        if (inv < 1) {
            error = error + "Inventory count cannot be less than 1.";
        }
        if (price <= 0) {
            error = error + "Price must be greater than $0";
        }
        if (max < min) {
            error = error + "Max must be greater than or equal to the Min.";
        }
        if (inv < min || inv > max) {
            error = error + "Inventory must be between the Min and Max values.";
        }
        if (sumOfParts > price) {
            error = error + "Price must be greater than sum of parts.";
        }
        return error;
    }
    }

