import Engine.ABSsystem;
import Engine.MainSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mainScene.MainSceneController;

import javax.print.DocFlavor;
import java.applet.Applet;
import java.awt.*;
import java.net.URL;

public class ABS_App extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //CSSFX.stsrt();

        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("mainScene.FXML");
        loader.setLocation(mainFXML);
        BorderPane root = loader.load(); //TODO: root should be border pane - we got scroll pane

        MainSceneController mainController = loader.getController();
        MainSystem engine = new ABSsystem(mainController); //TODO??
        mainController.setPrimaryStage(primaryStage);
        mainController.setModel(engine);

        primaryStage.setTitle("ABS System");
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
