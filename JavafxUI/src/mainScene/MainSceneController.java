package mainScene;

import Engine.MainSystem;
import adminScene.AdminSceneController;
import header.HeaderController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;



public class MainSceneController {

    @FXML private GridPane header;
    @FXML private HeaderController headerController;
    @FXML private ScrollPane adminScene;
    @FXML private AdminSceneController adminSceneController;

    private MainSystem model;
    private Stage primaryStage;

    @FXML public void initialize()
    {
        if(headerController != null && adminSceneController != null)
        {
            headerController.setParentController(this);
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
    public MainSystem getModel() { return model; }

    public void setFileInfo(String path)
    {
        headerController.setSelectedFilePathProperty(path);
        headerController.setIsFileSelectedProperty(true);
    }
}
