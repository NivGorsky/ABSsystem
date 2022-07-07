package adminBase;

import Engine.MainSystem;
import adminScene.AdminSceneController;
import exceptionDialog.ExceptionDialogCreator;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loginScene.LoginController;
import mutualInterfaces.ParentController;

public class adminBaseController implements ParentController {

    @FXML private ScrollPane adminMain;
    @FXML private BorderPane adminScene;
    @FXML private AdminSceneController adminSceneController;
    @FXML private ScrollPane login;
    @FXML private LoginController loginController;

    private MainSystem model;
    private Stage primaryStage;

    @FXML public void initialize() {
        if(adminSceneController != null && loginController != null) {
            adminSceneController.setParentController(this);
            loginController.setParentController(this);
        }
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
        this.adminMain = root;
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
