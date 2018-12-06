package C482TC.Model;

import javafx.beans.property.*;

public class Parts {

    private final IntegerProperty partID;
    private final StringProperty partName;
    private final DoubleProperty partPrice;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;

    // Construct

    public Parts() {
        partID = new SimpleIntegerProperty();
        partName = new SimpleStringProperty();
        partPrice = new SimpleDoubleProperty();
        inStock = new SimpleIntegerProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    // Get

    // PartID
    public IntegerProperty partIDProperty() {
        return partID;
    }

    public int getPartID(){
        return this.partID.get();
    }

    // PartName
    public StringProperty partNameProperty() {
        return partName;
    }

    public String getPartName(){
        return this.partName.get();
    }

    // PartPrice
    public DoubleProperty partPriceProperty() {
        return partPrice;
    }

    public double getPartPrice(){
        return this.partPrice.get();
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

    public int getMin() {
        return this.min.get();
    }

    // Max
    public IntegerProperty maxProperty() {
        return max;
    }

    public int getMax() {
        return this.max.get();
    }

    // Set

    // PartID
    public void setPartID(int partID){
        this.partID.set(partID);
    }

    // PartName
    public void setPartName(String name){
        this.partName.set(name);
    }

    // PartPrice
    public void setPartPrice(double price){
        this.partPrice.set(price);
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

    // Error Handling

    public static String isValid(String name, int min, int max, int inv, double price, String error){
        if(name == null){
            error = error + "Part name is required.";
        }
        if (inv < 1){
            error = error + "Inventory cannot be less than 1.";
        }
        if (price <= 0){
            error = error + "Price must be more than $0";
        }
        if (max < min){
            error = error + "The max must be greater than the min.";
        }
        if (inv < min || inv > max) {
            error = error + "Inventory must fall between Min and Max values.";
        }
        return error;
    }
}
