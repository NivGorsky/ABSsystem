package adminBase;

import Engine.ABSsystem;
import Engine.MainSystem;
import adminScene.AdminSceneController;
import exceptionDialog.ExceptionDialogCreator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loginScene.LoginController;
import mutualInterfaces.BaseController;
import mutualInterfaces.ParentController;

import java.net.URL;

public class AdminBaseController implements ParentController, BaseController {

    @FXML private ScrollPane root;
    @FXML private BorderPane adminScene;
    @FXML private AdminSceneController adminSceneController;
    @FXML private ScrollPane loginScene;
    @FXML private LoginController loginSceneController;

    @FXML public void initialize() {
        loadAdminScene();
        if(adminSceneController != null && loginSceneController != null) {
            adminSceneController.setParentController(this);
            loginSceneController.setParentController(this);
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
                root.setContent(loginScene);
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
    private void loadAdminScene(){
        try{
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("../adminScene/adminScene.fxml");
            loader.setLocation(mainFXML);
            adminScene = loader.load();
            adminSceneController = loader.getController();
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }

//        FXMLLoader loader = new FXMLLoader();
//
//        URL mainFXML = getClass().getResource("../adminBase/adminBase.fxml");
//        loader.setLocation(mainFXML);
//        ScrollPane root = loader.load();
//
//        AdminBaseController adminAppController = loader.getController();
//        MainSystem engine = new ABSsystem();
//        adminAppController.setPrimaryStage(primaryStage);
//        adminAppController.setModel(engine);
//        adminAppController.setRoot(root);
//        adminAppController.setPrimaryStage(primaryStage);
//
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
