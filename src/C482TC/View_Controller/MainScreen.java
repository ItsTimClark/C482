package C482TC.View_Controller;

import C482TC.Model.Inventory;
import C482TC.Model.Parts;
import C482TC.Model.Products;
import static C482TC.Model.Inventory.validatePartDelete;
import static C482TC.Model.Inventory.validateProductDelete;
import static C482TC.Model.Inventory.getPartsInventory;
import static C482TC.Model.Inventory.deletePart;
import static C482TC.Model.Inventory.getProductsInventory;
import static C482TC.Model.Inventory.removeProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreen implements Initializable{

    @FXML
    private TableView<Parts> parts;
    @FXML
    private TableColumn<Parts, Integer> partsIDColumn;
    @FXML
    private TableColumn<Parts, String> partsNameColumn;
    @FXML
    private TableColumn<Parts, Integer> partsInvColumn;
    @FXML
    private TableColumn<Parts, Double> partsPriceColumn;
    @FXML
    private TableView<Products> products;
    @FXML
    private TableColumn<Products, Integer> productsIDColumn;
    @FXML
    private TableColumn<Products, String> productsNameColumn;
    @FXML
    private TableColumn<Products, Integer> productsInvColumn;
    @FXML
    private TableColumn<Products, Double> productsPriceColumn;
    @FXML
    private TextField txtSearchParts;
    @FXML
    private TextField txtSearchProducts;

    private static Parts modifyParts;
    private static int modifyPartsIndex;
    private static Products modifyProducts;
    private static int modifyProductsIndex;

    public static int partsToModifyIndex(){
        return modifyPartsIndex;
    }

    public static int productsToModifyIndex(){
        return modifyProductsIndex;
    }

    // PARTS

    @FXML
    private void clearPartsSearch(ActionEvent event){
        updatePartsTableView();
        txtSearchParts.setText("");
    }

    @FXML
    private void handlePartsSearch(ActionEvent event) {
        String searchPart = txtSearchParts.getText();
        int partsIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error.");
            alert.setHeaderText("No parts found.");
            alert.setContentText("Search does not match any existing parts.");
            alert.showAndWait();
        } else {
            partsIndex = Inventory.lookupPart(searchPart);
            Parts tempPart = Inventory.getPartsInventory().get(partsIndex);
            ObservableList<Parts> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            parts.setItems(tempPartList);
        }
    }

        @FXML
        private void addPartsScreen(ActionEvent event) throws IOException
        {
            Parent addPartsParent = FXMLLoader.load(getClass().getResource("AddParts.fxml"));
            Scene addPartsScene = new Scene(addPartsParent);
            Stage addPartsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addPartsStage.setScene(addPartsScene);
            addPartsStage.show();
        }

        @FXML
        private void modifyPartsScreen(ActionEvent event) throws IOException
        {
            modifyParts = parts.getSelectionModel().getSelectedItem();
            modifyPartsIndex = getPartsInventory().indexOf(modifyParts);
            Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModifyParts.fxml"));
            Scene modifyPartsScene = new Scene(modifyPartParent);
            Stage modifyPartsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyPartsStage.setScene(modifyPartsScene);
            modifyPartsStage.show();
        }

    @FXML
    private void handlePartsDelete(ActionEvent event) {
        Parts part = parts.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Part cannot be deleted!");
            alert.setContentText("Part is being used by one or more products.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Confirm?");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deletePart(part);
                updatePartsTableView();
                System.out.println("Part " + part.getPartName() + " was removed.");
            }
            else {
                System.out.println("Part " + part.getPartName() + " was not removed.");
            }
        }
    }

        // PRODUCTS

        @FXML
        private void clearProductsSearch(ActionEvent event){
        updateProductsTableView();
            txtSearchProducts.setText("");
        }

        @FXML
        private void handleProductsSearch(ActionEvent event){
        String searchProduct = txtSearchProducts.getText();
        int productIndex = -1;
        if (Inventory.lookupProduct(searchProduct) == -1)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error.");
            alert.setHeaderText("No products found.");
            alert.setContentText("Search does not match any existing products.");
            alert.showAndWait();
        }
        else{
            productIndex = Inventory.lookupProduct(searchProduct);
            Products tempProduct = Inventory.getProductsInventory().get(productIndex);
            ObservableList<Products> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            products.setItems(tempProductList);
        }
        }

        @FXML
        private void handleProductsDelete(ActionEvent event){
        Products product = products.getSelectionModel().getSelectedItem();
        if(validateProductDelete(product))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error.");
            alert.setHeaderText("Products cannot be deleted.");
            alert.setContentText("Product contains at least one part.");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Deletion");
            alert.setHeaderText("Confirm Delete?");
            alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                removeProduct(product);
                updateProductsTableView();
                System.out.println("Product " + product.getProductName() + " was removed.");
            } else {
                System.out.println("Product " + product.getProductName() + " was removed.");
            }
        }
        }

        @FXML
        private void addProductsScreen(ActionEvent event) throws IOException
        {
            Parent addProductsParent = FXMLLoader.load(getClass().getResource("AddProducts.fxml"));
            Scene addProductsScene = new Scene(addProductsParent);
            Stage addProductsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            addProductsStage.setScene(addProductsScene);
            addProductsStage.show();
        }

        @FXML
        private void modifyProductsScreen(ActionEvent event) throws IOException
        {
            modifyProducts = products.getSelectionModel().getSelectedItem();
            modifyProductsIndex = getProductsInventory().indexOf(modifyProducts);
            Parent modifyProductsParent = FXMLLoader.load(getClass().getResource("ModifyProducts.fxml"));
            Scene modifyProductsScene = new Scene(modifyProductsParent);
            Stage modifyProductsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyProductsStage.setScene(modifyProductsScene);
            modifyProductsStage.show();
    }

    public void updatePartsTableView() {
        parts.setItems(getPartsInventory());
    }

    public void updateProductsTableView() {
        products.setItems(getProductsInventory());
    }

    @FXML
    private void exitButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Exit Confirmation");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else {
            System.out.println("Canceled.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partsIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        partsNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        partsInvColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        partsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        productsIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        productsNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        productsInvColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        productsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartsTableView();
        updateProductsTableView();
    }
}
