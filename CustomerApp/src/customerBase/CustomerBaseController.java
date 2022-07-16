package customerBase;

import Engine.MainSystem;
import customerScene.CustomerSceneController;
import exceptionDialog.ExceptionDialogCreator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loginScene.LoginController;
import mutualInterfaces.BaseController;
import mutualInterfaces.ParentController;

import java.net.URL;

public class CustomerBaseController implements ParentController, BaseController {

    @FXML private ScrollPane root;
    @FXML private ScrollPane loginScene;
    @FXML private LoginController loginSceneController;
    @FXML private BorderPane customerScene;
    @FXML private CustomerSceneController customerSceneController;
    
    private MainSystem model;
    private Stage primaryStage;
    private BooleanProperty isLoggedIn;

    @FXML public void initialize()
    {
        loadCustomerScene();
        if(loginSceneController != null && customerSceneController != null)
        {
            customerSceneController.setParentController(this);
            loginSceneController.setParentController(this);
            loginSceneController.setLoginType(LoginController.LoginType.CUSTOMER);
        }
    }

    public CustomerBaseController(){
        isLoggedIn = new SimpleBooleanProperty();
        isLoggedIn.addListener(((observable, oldValue, newValue) -> {

            if(newValue == true){
                customerSceneController.setParentController(this);
                customerSceneController.initializeTabs();
                root.setContent(customerScene);
                customerSceneController.onShow();
                customerSceneController.startRefresher();
            }

            else{
                root.setContent(loginScene);
            }
        }));

    }

    public void setPrimaryStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }
    public void setModel(MainSystem model) {
        this.model = model;
    }
    public void setRoot(ScrollPane root){
        this.root = root;
    }
    public void setFileInfo(String path) {
        customerSceneController.initializeTabs();
    }
    private void loadCustomerScene(){
        try{
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("../customerScene/customerScene.fxml");
            loader.setLocation(mainFXML);
            customerScene = loader.load();
            customerSceneController = loader.getController();
        }

        catch (Exception ex){
            createExceptionDialog(ex);
        }
    }

    @Override
    public void createExceptionDialog(Exception ex) {
        ExceptionDialogCreator.createExceptionDialog(ex);
    }

    @Override
    public Stage getPrimaryStage() { return primaryStage; }

    @Override
    public void switchStyleSheet(String selectedItem) {
        switch (selectedItem)
        {
            case("Light Mode"):
            {
                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(
                        getClass().getResource("/css/LightMode.css").toExternalForm());

                break;
            }
            case ("Dark Mode"):
            {
                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(
                        getClass().getResource("/css/DarkMode.css").toExternalForm());

                break;
            }
            case ("MTA Mode"):
            {

                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(
                        getClass().getResource("/css/MTAMode.css").toExternalForm());

                break;
            }
            case ("Barbi Mode"):
            {
                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(
                        getClass().getResource("/css/BarbiMode.css").toExternalForm());

                break;
            }
        }
    }

    @Override
    public void setLoggedInDetails(String name) {
        customerSceneController.setCustomer(name);
        isLoggedIn.set(true);
    }

    @Override
    public String getLoggedInUser() {
        return customerSceneController.getLoggedInUser();
    }


}
