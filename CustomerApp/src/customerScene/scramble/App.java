package customerScene.scramble;
import Engine.ABSsystem;
import Engine.MainSystem;
import customerScene.loanTrading.LoanTradingSceneController;
import customerScene.scramble.ScrambleController;
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

        URL mainFXML = getClass().getResource("scramble.fxml");
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        ScrambleController scrambleController = loader.getController();
        MainSystem engine = new ABSsystem();
//        scrambleController.setPrimaryStage(primaryStage);
//        scrambleController.setModel(engine);
//        scrambleController.setRoot(root);
//        scrambleController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("ABS System");
        Scene scene = new Scene(root, 1280, 650);
        scene.getStylesheets().add(
                getClass().getResource("/css/LightMode.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
