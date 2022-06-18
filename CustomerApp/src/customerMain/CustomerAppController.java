package customerMain;

import Engine.MainSystem;
import customer.CustomerController;
import exceptionDialog.ExceptionDialogCreator;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import loginScene.LoginController;
import mutualInterfaces.ParentController;

public class CustomerAppController implements ParentController {

    @FXML private ScrollPane root;

    @FXML private ScrollPane login;
    @FXML private LoginController loginController;

    @FXML private BorderPane customerScene;
    @FXML private CustomerController customerController;


    private MainSystem model;
    private Stage primaryStage;

    @FXML public void initialize()
    {
        if(loginController != null && customerController != null)
        {
            loginController.setParentController(this);
            customerController.setParentController(this);

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

    public void setFileInfo(String path) {
        customerController.initializeTabs();
    }

    @Override
    public void createExceptionDialog(Exception ex) {
        ExceptionDialogCreator.createExceptionDialog(ex);
    }

    @Override
    public MainSystem getModel() { return model; }

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
}
