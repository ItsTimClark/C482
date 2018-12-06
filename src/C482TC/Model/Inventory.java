package C482TC.Model;

import javafx.collections.*;

public class Inventory {

    private static ObservableList<Products> productsInventory = FXCollections.observableArrayList();
    private static ObservableList<Parts> partsInventory = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    private static int productIDCount = 0;

    public static ObservableList<Parts> getPartsInventory(){
        return partsInventory;
    }

    public static void addPart(Parts parts){
        partsInventory.add(parts);
    }

    public static void deletePart(Parts parts){
        partsInventory.remove(parts);
    }

    public static void updatePart(int index, Parts parts){
        partsInventory.set(index, parts);
    }

    public static int getPartIDCount(){
        partIDCount++;
        return partIDCount;
    }

    public static boolean validatePartDelete(Parts parts){
        boolean isFound = false;
        for (int i=0; i < productsInventory.size(); i++){
            if(productsInventory.get(i).getProductParts().contains(parts)){
                isFound=true;
            }
        }
        return isFound;
    }

    public static boolean validateProductDelete(Products products) {
        boolean isFound = false;
        int productID = products.getProductID();
        for (int i=0; i < productsInventory.size(); i++) {
            if (productsInventory.get(i).getProductID() == productID) {
                if (!productsInventory.get(i).getProductParts().isEmpty()) {
                    isFound = true;
                }
            }
        }
        return isFound;
    }
    public static int lookupPart(String searchTerm) {
        boolean isFound = false;
        int index = 0;
        if (isInteger(searchTerm)) {
            for (int i = 0; i < partsInventory.size(); i++) {
                if (Integer.parseInt(searchTerm) == partsInventory.get(i).getPartID()) {
                    index = i;
                    isFound = true;
                }
            }
        }
        else {
            for (int i = 0; i < partsInventory.size(); i++) {
                searchTerm = searchTerm.toLowerCase();
                if (searchTerm.equals(partsInventory.get(i).getPartName().toLowerCase())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound == true) {
            return index;
        }
        else {
            System.out.println("Parts not found.");
            return -1;
        }
    }

    public static ObservableList<Products> getProductsInventory() {
        return productsInventory;
    }

    public static void addProduct(Products products) {
        productsInventory.add(products);
    }

    public static void removeProduct(Products products) {
        productsInventory.remove(products);
    }

    public static int getProductIDCount() {
        productIDCount++;
        return productIDCount;
    }

    public static int lookupProduct(String searchTerm) {
        boolean isFound = false;
        int index = 0;
        if (isInteger(searchTerm)) {
            for (int i = 0; i < productsInventory.size(); i++) {
                if (Integer.parseInt(searchTerm) == productsInventory.get(i).getProductID()) {
                    index = i;
                    isFound = true;
                }
            }
        }
        else {
            for (int i = 0; i < productsInventory.size(); i++) {
                if (searchTerm.equals(productsInventory.get(i).getProductName())) {
                    index = i;
                    isFound = true;
                }
            }
        }

        if (isFound == true) {
            return index;
        }
        else {
            System.out.println("No products found.");
            return -1;
        }
    }

    public static void updateProduct(int index, Products products) {
        productsInventory.set(index, products);
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
