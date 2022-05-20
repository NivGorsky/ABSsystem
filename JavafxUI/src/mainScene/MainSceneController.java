package mainScene;

import Engine.MainSystem;
import adminScene.AdminSceneController;
import customer.CustomerController;
import header.HeaderController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;


public class MainSceneController {

    @FXML private GridPane header;
    @FXML private HeaderController headerController;

    @FXML private ScrollPane adminScene;
    @FXML private AdminSceneController adminSceneController;
    @FXML private TabPane customerPane;
    @FXML private CustomerController customerPaneController;
    @FXML private BorderPane borderPane;
    @FXML private AnchorPane centerAnchorPane;
    @FXML private ScrollPane root;

    private MainSystem model;
    private Stage primaryStage;

    @FXML public void initialize()
    {
        if(headerController != null && adminSceneController != null)
        {
            headerController.setParentController(this);
            adminSceneController.setParentController(this);
            loadCustomer();
            customerPaneController.getCustomerNameProperty().bind(headerController.getChosenCustomerNameProperty());
        }
    }

    public HeaderController getHeaderController(){return headerController;}
    public void setMainStage(Stage stage){this.primaryStage = stage;}
    public void setPrimaryStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }
    public void setModel(MainSystem model)
    {
        this.model = model;
        headerController.setModel(model);
        customerPaneController.setModel(model);
    }
    public void setRoot(ScrollPane root){
        this.root = root;
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
    public void switchBody(String selectedItemInComboBox) {
        switch (selectedItemInComboBox) {
            case "Admin":
                adminSceneController.onShow();
                centerAnchorPane.getChildren().clear();
                centerAnchorPane.getChildren().add(adminScene);
                break;

                //customer
            default:
                centerAnchorPane.getChildren().clear();
                centerAnchorPane.getChildren().add(customerPane);
        }

    }

}
