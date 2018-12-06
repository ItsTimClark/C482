package C482TC.View_Controller;

import C482TC.Model.InHouseParts;
import C482TC.Model.Inventory;
import C482TC.Model.OutsourceParts;
import C482TC.Model.Parts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static C482TC.Model.Inventory.getPartsInventory;
import static C482TC.View_Controller.MainScreen.partsToModifyIndex;

public class ModifyParts implements Initializable {

    private boolean isOutsourced;
    int partIndex = partsToModifyIndex();
    private String exceptionMessage = new String();
    private int partID;

    @FXML
    private RadioButton radioModifyPartInHouse;
    @FXML
    private RadioButton radioModifyPartOutsourced;
    @FXML
    private Label lblModifyPartIDNumber;
    @FXML
    private TextField txtModifyPartName;
    @FXML
    private TextField txtModifyPartInv;
    @FXML
    private TextField txtModifyPartPrice;
    @FXML
    private TextField txtModifyPartMin;
    @FXML
    private TextField txtModifyPartMax;
    @FXML
    private Label lblModifyPartDyn;
    @FXML
    private TextField txtModifyPartDyn;

    @FXML
    void modifyPartsInHouseRadio(ActionEvent event) {
        isOutsourced = false;
        radioModifyPartOutsourced.setSelected(false);
        lblModifyPartDyn.setText("Machine ID");
        txtModifyPartDyn.setText("");
        txtModifyPartDyn.setPromptText("Machine ID");
    }

    @FXML
    void modifyPartsOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        radioModifyPartInHouse.setSelected(false);
        lblModifyPartDyn.setText("Company Name");
        txtModifyPartDyn.setText("");
        txtModifyPartDyn.setPromptText("Company Name");
    }

    @FXML
    void handleModifyPartSave(ActionEvent event) throws IOException {
        String partName = txtModifyPartName.getText();
        String partInv = txtModifyPartInv.getText();
        String partPrice = txtModifyPartPrice.getText();
        String partMin = txtModifyPartMin.getText();
        String partMax = txtModifyPartMax.getText();
        String partDyn = txtModifyPartDyn.getText();

        try {
            exceptionMessage = Parts.isValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                if (isOutsourced == false) {
                    System.out.println("Part name: " + partName);
                    InHouseParts inPart = new InHouseParts();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setInStock(Integer.parseInt(partInv));
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setMin(Integer.parseInt(partMin));
                    inPart.setMax(Integer.parseInt(partMax));
                    inPart.setPartID(Integer.parseInt(partDyn));
                    Inventory.updatePart(partIndex, inPart);
                }
                else {
                    System.out.println("Part name: " + partName);
                    OutsourceParts outPart = new OutsourceParts();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setInStock(Integer.parseInt(partInv));
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setMin(Integer.parseInt(partMin));
                    outPart.setMax(Integer.parseInt(partMax));
                    outPart.setOutsourceCompanyName(partDyn);
                    Inventory.updatePart(partIndex, outPart);
                }

                Parent modifyProductSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Part");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleModifyPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel modifying the part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent modifyPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyPartCancel);
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
        Parts part = getPartsInventory().get(partIndex);
        partID = getPartsInventory().get(partIndex).getPartID();
        lblModifyPartIDNumber.setText("Auto-Gen: " + partID);
        txtModifyPartName.setText(part.getPartName());
        txtModifyPartInv.setText(Integer.toString(part.getInStock()));
        txtModifyPartPrice.setText(Double.toString(part.getPartPrice()));
        txtModifyPartMin.setText(Integer.toString(part.getMin()));
        txtModifyPartMax.setText(Integer.toString(part.getMax()));
        if (part instanceof InHouseParts) {
            lblModifyPartDyn.setText("Machine ID");
            txtModifyPartDyn.setText(Integer.toString(((InHouseParts) getPartsInventory().get(partIndex)).getPartID()));
            radioModifyPartInHouse.setSelected(true);
        }
        else {
            lblModifyPartDyn.setText("Company Name");
            txtModifyPartDyn.setText(((OutsourceParts) getPartsInventory().get(partIndex)).getOutsourceCompanyName());
            radioModifyPartOutsourced.setSelected(true);
        }
    }
}