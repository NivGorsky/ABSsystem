package adminBase;

import Engine.MainSystem;
import adminScene.AdminSceneController;
import exceptionDialog.ExceptionDialogCreator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loginScene.LoginController;
import mutualInterfaces.BaseController;
import mutualInterfaces.ParentController;

public class AdminBaseController implements ParentController, BaseController {

    @FXML private ScrollPane root;
    @FXML private BorderPane adminScene;
    @FXML private AdminSceneController adminSceneController;
    @FXML private ScrollPane login;
    @FXML private LoginController loginController;

    @FXML public void initialize() {
        if(adminSceneController != null && loginController != null) {
            adminSceneController.setParentController(this);
            loginController.setParentController(this);
        }
    }

    private BooleanProperty isLoggedIn;
    private MainSystem model;
    private Stage primaryStage;

    public AdminBaseController(){
        isLoggedIn = new SimpleBooleanProperty();
        isLoggedIn.addListener(((observable, oldValue, newValue) -> {

            if(newValue == true){
                root.setContent(adminScene);
            }

            else{
                root.setContent(login);
            }
        }));
    }

    @Override
    public MainSystem getModel() {
        return model;
    }

    @Override
    public void createExceptionDialog(Exception ex) {
        ExceptionDialogCreator.createExceptionDialog(ex);
    }

    @Override
    public Stage getPrimaryStage() {
        return primaryStage;
    }

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

    public void setIsLoggedInProperty(Boolean newValue){
        isLoggedIn.set(newValue);
    }



    //    public void switchBody(String selectedItemInComboBox) {
//        switch (selectedItemInComboBox) {
//            case "Admin":
//                adminSceneController.onShow();
//                centerAnchorPane.getChildren().clear();
//                centerAnchorPane.getChildren().add(adminScene);
//                break;
//
//            //customer
//            default:
//                centerAnchorPane.getChildren().clear();
//                customerPaneController.chooseTab(0);
//                centerAnchorPane.getChildren().add(customerPane);
//        }
//    }
}
