package main;
import adminBase.AdminBaseController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
public class AdminMain extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("/adminBase/adminBase.fxml");
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        AdminBaseController adminAppController = loader.getController();
        adminAppController.setPrimaryStage(primaryStage);
        adminAppController.setRoot(root);
        adminAppController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("ABS System");
        Scene scene = new Scene(root, 1280, 650);
        scene.getStylesheets().add(
                getClass().getResource("/css/LightMode.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                adminAppController.adminLoggedOut();
            }
        });
    }
}