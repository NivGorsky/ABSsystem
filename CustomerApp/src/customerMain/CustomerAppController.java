package customerMain;

import header.HeaderController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CustomerAppController implements ParentController{

    @FXML private ScrollPane root;

    @FXML private ScrollPane login;
    @FXML private LoginController loginController;

    @FXML private GridPane header;
    @FXML private HeaderController headerController;

    @FXML private TabPane customerTabPane;
    @FXML private CustomerController customerController;


    private MainSystem model;
    private Stage primaryStage;

    @FXML public void initialize()
    {
        if(headerController != null && loginController != null && customerController != null)
        {
            headerController.setParentController(this);
            loginController.setParentController(this);
            customerController.setParentController(this);

        }

    }

    public void setFileInfo(String path)
    {
        headerController.setIsFileSelectedProperty(true);
        customerController.initializeTabs();
    }

    public HeaderController getHeaderController(){return headerController;}
    public void setMainStage(Stage stage){this.primaryStage = stage;}
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
    public Stage getPrimaryStage() { return primaryStage; }
    public MainSystem getModel() { return model; }
}
