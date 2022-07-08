package loginScene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import mutualInterfaces.BaseController;
import mutualInterfaces.ParentController;

public class LoginController {

    @FXML private ScrollPane login;
    @FXML private Label loginLabel;
    @FXML private Label nameLabel;
    @FXML private TextField nameTextField;
    @FXML private Button logInButton;

    @FXML
    void loginButtonClicked(ActionEvent event) {
        tryLogIn();
    }



    public LoginController(){

    }

    private ParentController parentController;
    private BaseController baseController;

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
        this.baseController = (BaseController)parentController;
    }

    private void tryLogIn(){


        baseController.setIsLoggedInProperty(true);
    }


}
