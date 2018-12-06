package C482TC.View_Controller;

import C482TC.Model.Parts;
import C482TC.Model.Products;
import C482TC.Model.Inventory;
import C482TC.Model.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static C482TC.Model.Inventory.getPartsInventory;

public class AddProducts implements Initializable{

    private ObservableList<Parts> currentParts = FXCollections.observableArrayList();
    private String exceptionMessage = new String();
    private int productID;

    @FXML
    private Label lblAddProductsIDNumber;
    @FXML
    private TextField txtAddProductsName;
    @FXML
    private TextField txtAddProductsInv;
    @FXML
    private TextField txtAddProductsPrice;
    @FXML
    private TextField txtAddProductsMin;
    @FXML
    private TextField txtAddProductsMax;
    @FXML
    private TextField txtAddProductsSearch;
    @FXML
    private TableView<Parts> addProducts;
    @FXML
    private TableColumn<Parts, Integer> addProductsAddIDColumn;
    @FXML
    private TableColumn<Parts, String> addProductsAddNameColumn;
    @FXML
    private TableColumn<Parts, Integer> addProductsAddInvColumn;
    @FXML
    private TableColumn<Parts, Double> addProductsAddPriceColumn;
    @FXML
    private TableView<Parts> deleteProducts;
    @FXML
    private TableColumn<Parts, Integer> deleteProductsIDColumn;
    @FXML
    private TableColumn<Parts, String> deleteProductsNameColumn;
    @FXML
    private TableColumn<Parts, Integer> deleteProductsInvColumn;
    @FXML
    private TableColumn<Parts, Double> deleteProductsPriceColumn;

    @FXML
    void handleClearSearch(ActionEvent event) {
        updateAddPartsTableView();
        txtAddProductsSearch.setText("");
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchPart = txtAddProductsSearch.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error.");
            alert.setHeaderText("No parts found.");
            alert.setContentText("No parts returned by search.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPart);
            Parts tempPart = getPartsInventory().get(partIndex);
            ObservableList<Parts> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            addProducts.setItems(tempPartList);
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        Parts part = addProducts.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateDeletePartsTableView();
    }

    @FXML
    void handleDelete(ActionEvent event) {
        Parts part = deleteProducts.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete a Part");
        alert.setHeaderText("Please Confirm");
        alert.setContentText("Are you sure you want to delete " + part.getPartName() + " from part list?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Part has been deleted.");
            currentParts.remove(part);
        }
        else {
            System.out.println("Part deletion cancelled.");
        }
    }

    @FXML
    void handleAddProductsSave(ActionEvent event) throws IOException {
        String productName = txtAddProductsName.getText();
        String productInv = txtAddProductsInv.getText();
        String productPrice = txtAddProductsPrice.getText();
        String productMin = txtAddProductsMin.getText();
        String productMax = txtAddProductsMax.getText();

        try{

            if (currentParts.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Product must contain at least one part.");
                alert.showAndWait();
            }
            else {
                System.out.println("Product name: " + productName);
                Products newProduct = new Products();
                newProduct.setProductID(productID);
                newProduct.setProductName(productName);
                newProduct.setInStock(Integer.parseInt(productInv));
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setMin(Integer.parseInt(productMin));
                newProduct.setMax(Integer.parseInt(productMax));
                newProduct.setProductParts(currentParts);
                Inventory.addProduct(newProduct);

                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Product");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddProductsCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancellation");
        alert.setHeaderText("Please Confirm Cancellation.");
        alert.setContentText("Are you sure you want to cancel adding a new product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addProductCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You clicked cancel.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addProductsAddIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        addProductsAddNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        addProductsAddInvColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        addProductsAddPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        deleteProductsIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        deleteProductsNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        deleteProductsInvColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        deleteProductsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updateAddPartsTableView();
        updateDeletePartsTableView();
        productID = Inventory.getProductIDCount();
        lblAddProductsIDNumber.setText("Auto-Gen: " + productID);
    }

    public void updateAddPartsTableView() {
        addProducts.setItems(getPartsInventory());
    }

    public void updateDeletePartsTableView() {
        deleteProducts.setItems(currentParts);
    }
}
