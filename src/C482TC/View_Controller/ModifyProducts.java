package C482TC.View_Controller;

import C482TC.Model.*;
import C482TC.Model.Inventory;
import C482TC.Model.Parts;
import C482TC.Model.Products;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static C482TC.View_Controller.MainScreen.productsToModifyIndex;
import static C482TC.Model.Inventory.getPartsInventory;
import static C482TC.Model.Inventory.getProductsInventory;

public class ModifyProducts implements Initializable {private ObservableList<Parts> currentParts = FXCollections.observableArrayList();
    private int productIndex = productsToModifyIndex();
    private String exceptionMessage = new String();
    private int productID;

    @FXML
    private Label lblModifyProductIDNumber;
    @FXML
    private TextField txtModifyProductName;
    @FXML
    private TextField txtModifyProductInv;
    @FXML
    private TextField txtModifyProductPrice;
    @FXML
    private TextField txtModifyProductMin;
    @FXML
    private TextField txtModifyProductMax;
    @FXML
    private TextField txtModifyProductSearch;
    @FXML
    private TableView<Parts> modifyProductAdd;
    @FXML
    private TableColumn<Parts, Integer> modifyProductAddIDColumn;
    @FXML
    private TableColumn<Parts, String> modifyProductAddNameColumn;
    @FXML
    private TableColumn<Parts, Integer> modifyProductAddInvColumn;
    @FXML
    private TableColumn<Parts, Double> modifyProductAddPriceColumn;
    @FXML
    private TableView<Parts> modifyProductDelete;
    @FXML
    private TableColumn<Parts, Integer> modifyProductDeleteIDColumn;
    @FXML
    private TableColumn<Parts, String> modifyProductDeleteNameColumn;
    @FXML
    private TableColumn<Parts, Integer> modifyProductDeleteInvColumn;
    @FXML
    private TableColumn<Parts, Double> modifyProductDeletePriceColumn;

    @FXML
    void handleClearSearch(ActionEvent event) {
        updateAddPartsTableView();
        txtModifyProductSearch.setText("");
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchPart = txtModifyProductSearch.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any known parts.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPart);
            Parts tempPart = Inventory.getPartsInventory().get(partIndex);
            ObservableList<Parts> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            modifyProductAdd.setItems(tempPartList);
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        Parts part = modifyProductAdd.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateDeletePartsTableView();
    }

    @FXML
    void handleDelete(ActionEvent event) {
        Parts part = modifyProductDelete.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Part Deletion");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to delete " + part.getPartName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            currentParts.remove(part);
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }

    @FXML
    private void handleModifyProductSave(ActionEvent event) throws IOException {
        String productName = txtModifyProductName.getText();
        String productInv = txtModifyProductInv.getText();
        String productPrice = txtModifyProductPrice.getText();
        String productMin = txtModifyProductMin.getText();
        String productMax = txtModifyProductMax.getText();

        try {
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
                Inventory.updateProduct(productIndex, newProduct);

                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Product");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleModifyProductCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel modifying the product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent modifyProductCancelParent = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyProductCancelParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Products product = getProductsInventory().get(productIndex);
        productID = getProductsInventory().get(productIndex).getProductID();
        lblModifyProductIDNumber.setText("Auto-Gen: " + productID);
        txtModifyProductName.setText(product.getProductName());
        txtModifyProductInv.setText(Integer.toString(product.getInStock()));
        txtModifyProductPrice.setText(Double.toString(product.getProductPrice()));
        txtModifyProductMin.setText(Integer.toString(product.getMin()));
        txtModifyProductMax.setText(Integer.toString(product.getMax()));
        currentParts = product.getProductParts();
        modifyProductAddIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        modifyProductAddNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        modifyProductAddInvColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        modifyProductAddPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        modifyProductDeleteIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        modifyProductDeleteNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        modifyProductDeleteInvColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        modifyProductDeletePriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        updateAddPartsTableView();
        updateDeletePartsTableView();
    }

    public void updateAddPartsTableView() {
        modifyProductAdd.setItems(getPartsInventory());
    }

    public void updateDeletePartsTableView() {
        modifyProductDelete.setItems(currentParts);
    }
}