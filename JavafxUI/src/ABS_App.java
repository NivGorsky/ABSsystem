import Engine.ABSsystem;
import Engine.MainSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import mainScene.MainSceneController;

import java.net.URL;

public class ABS_App extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("/mainScene/mainScene.FXML");
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        MainSceneController mainController = loader.getController();
        MainSystem engine = new ABSsystem();
        mainController.setPrimaryStage(primaryStage);
        mainController.setModel(engine);
        mainController.setRoot(root);
        mainController.setMainStage(primaryStage);

        primaryStage.setTitle("ABS System");
        Scene scene = new Scene(root, 1280, 650);
        scene.getStylesheets().add(
                getClass().getResource("/css/LightMode.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
