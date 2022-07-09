package customerScene.information;
import Engine.ABSsystem;
import Engine.MainSystem;
import customerBase.CustomerBaseController;
import customerScene.createLoanScene.CreateLoanSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import java.net.URL;
public class App extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        URL mainFXML = getClass().getResource("information.fxml");
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        InformationController informationController = loader.getController();
        MainSystem engine = new ABSsystem();
//        informationController.setPrimaryStage(primaryStage);
//        informationController.setModel(engine);
//        informationController.setRoot(root);
//        informationController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("ABS System");
        Scene scene = new Scene(root, 1280, 650);
        scene.getStylesheets().add(
                getClass().getResource("/css/LightMode.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}