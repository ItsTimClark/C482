package C482TC.View_Controller;

import C482TC.Model.Parts;
import C482TC.Model.OutsourceParts;
import C482TC.Model.InHouseParts;
import C482TC.Model.Inventory;
import C482TC.Model.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AddParts implements Initializable{
    @FXML
    private RadioButton radioAddPartsInHouse;
    @FXML
    private RadioButton radioAddPartsOutsource;
    @FXML
    private Label lblAddPartsIDNumber;
    @FXML
    private TextField txtAddPartsName;
    @FXML
    private TextField txtAddPartsInv;
    @FXML
    private TextField txtAddPartsPrice;
    @FXML
    private TextField txtAddPartsMin;
    @FXML
    private TextField txtAddPartsMax;
    @FXML
    private Label lblAddPartsDyn;
    @FXML
    private TextField txtAddPartsDyn;

    private boolean isOutsource;
    private String exceptionMessage = new String();
    private int partID;

    @FXML
    void addPartsInHouseRadio(ActionEvent event){
        isOutsource = false;
        lblAddPartsDyn.setText("Machine ID");
        txtAddPartsDyn.setPromptText("Machine ID");
        radioAddPartsOutsource.setSelected(false);
    }

    @FXML
    void addPartsOutsourceRadio(ActionEvent event){
        isOutsource = true;
        lblAddPartsDyn.setText("Company Name");
        txtAddPartsDyn.setPromptText("Company Name");
        radioAddPartsInHouse.setSelected(false);
    }
    @FXML
    void handleAddPartsSave(ActionEvent event) throws IOException {
        String partName = txtAddPartsName.getText();
        String partInv = txtAddPartsInv.getText();
        String partPrice = txtAddPartsPrice.getText();
        String partMin = txtAddPartsMin.getText();
        String partMax = txtAddPartsMax.getText();
        String partDyn = txtAddPartsDyn.getText();

        try {
            exceptionMessage = Parts.isValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                if (isOutsource == false) {
                    System.out.println("Part name: " + partName);
                    InHouseParts inPart = new InHouseParts();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setInStock(Integer.parseInt(partInv));
                    inPart.setMin(Integer.parseInt(partMin));
                    inPart.setMax(Integer.parseInt(partMax));
                    inPart.setPartID(Integer.parseInt(partDyn));
                    Inventory.addPart(inPart);
                } else {
                    System.out.println("Part name: " + partName);
                    OutsourceParts outPart = new OutsourceParts();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setInStock(Integer.parseInt(partInv));
                    outPart.setMin(Integer.parseInt(partMin));
                    outPart.setMax(Integer.parseInt(partMax));
                    outPart.setOutsourceCompanyName(partDyn);
                    Inventory.addPart(outPart);
                }

                Parent addPartSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addPartSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAddPartsCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancellation");
        alert.setHeaderText("Please Confirm Cancellation.");
        alert.setContentText("Are you sure you want to cancel adding a new part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addPartCancel);
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
        partID = Inventory.getPartIDCount();
        lblAddPartsIDNumber.setText("Auto-Gen: " + partID);
    }
}