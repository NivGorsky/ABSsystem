package mainScene;

import Engine.MainSystem;
import adminScene.AdminSceneController;
import customer.CustomerController;
import header.HeaderController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;


public class MainSceneController {

    @FXML private GridPane header;
    @FXML private HeaderController headerController;
    @FXML private ScrollPane adminScene;
    @FXML private AdminSceneController adminSceneController;
    @FXML private ScrollPane customerPane;
    @FXML private CustomerController customerPaneController;

    private MainSystem model;
    private Stage primaryStage;

    @FXML public void initialize()
    {
        if(headerController != null && adminSceneController != null)
        {
            headerController.setParentController(this);
            adminSceneController.setParentController(this);
            loadCustomer();
        }
    }

    public void setMainStage(Stage stage){this.primaryStage = stage;}
    public void setPrimaryStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }
    public void setModel(MainSystem model)
    {
        this.model = model;
        headerController.setModel(model);
    }
    public Stage getPrimaryStage() { return primaryStage; }
    public MainSystem getModel() { return model; }
    public void setFileInfo(String path)
    {
        headerController.setSelectedFilePathProperty(path);
        headerController.setIsFileSelectedProperty(true);
    }
    private void loadCustomer(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/customer/Customer.fxml");
            fxmlLoader.setLocation(url);
            customerPane = fxmlLoader.load();
            customerPaneController = fxmlLoader.getController();
            customerPaneController.setParent(this);
        }

        catch (Exception e){

        }
    }
    public void switchBody(String selectedItemInComboBox){
        Scene scene;

        switch(selectedItemInComboBox){
            case "Admin":
                scene = new Scene(adminScene, 700, 600);
                break;

            default:
                scene = new Scene(customerPane, 700, 600);
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
