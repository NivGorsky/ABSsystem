package mainScene;

import Engine.MainSystem;
import adminScene.AdminSceneController;
import header.HeaderPaneController;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainSceneController {

    @FXML private GridPane headerPane;
    @FXML private HeaderPaneController headerPaneController;
    @FXML private GridPane adminScene;
    @FXML private AdminSceneController adminSceneController;

    private MainSystem model;
    private Stage primaryStage;


    @FXML public void initialize()
    {
        if(headerPaneController != null && adminSceneController != null)
        {
            headerPaneController.setParentController(this);
            adminSceneController.setParentController(this);
        }
    }

    public void setPrimaryStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }
    public void setModel(MainSystem model)
    {
        this.model = model;
    }

    public Stage getPrimaryStage() { return primaryStage; }
    public HeaderPaneController getHeaderPaneController() { return headerPaneController; }
    public AdminSceneController getAdminPaneController() { return adminSceneController; }
    public MainSystem getModel() { return model; }
}
