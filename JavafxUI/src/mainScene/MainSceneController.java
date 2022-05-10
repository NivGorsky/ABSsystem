package mainScene;

import Engine.MainSystem;
import adminScene.AdminSceneController;
import generalScenes.HeaderPaneController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.naming.Binding;

public class MainSceneController {

    @FXML private GridPane headerPane;
    @FXML private HeaderPaneController headerPaneController;
    @FXML private GridPane adminScene;
    @FXML private AdminSceneController adminSceneController;


    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFilePath;
    private SimpleIntegerProperty currentYAZ;

    private MainSystem model;
    private Stage primaryStage;


    public MainSceneController()
    {
        isFileSelected = new SimpleBooleanProperty();
        selectedFilePath = new SimpleStringProperty();
        currentYAZ = new SimpleIntegerProperty();
    }

    @FXML public void initialize()
    {
        headerPaneController.getFilePathLabel().textProperty().bind(Bindings.format("File Path: ", selectedFilePath));
        headerPaneController.getCurrentYazLabel().textProperty().bind(Bindings.format("Current YAZ: ", currentYAZ));
    }

    public void setPrimaryStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }

    public void setModel(MainSystem model)
    {
        this.model = model;
    }




}
