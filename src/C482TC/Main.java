package C482TC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception{
    Parent root = FXMLLoader.load(getClass().getResource("/C482TC/View_Controller/MainScreen.fxml"));
    Scene scene = new Scene(root);
    primaryStage.setTitle("Inventory Management System");
    primaryStage.setScene(scene);
    primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    }
