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

    public enum LoginType {
        ADMIN,
        CUSTOMER
    }

    @FXML private ScrollPane login;
    @FXML private Label loginLabel;
    @FXML private Label nameLabel;
    @FXML private TextField nameTextField;
    @FXML private Button logInButton;

    private ParentController parentController;
    private BaseController baseController;
    private LoginType loginType;

    public void setLoginType(LoginType type) { this.loginType = type; }

    @FXML
    public void loginButtonClicked(ActionEvent event) {
        if(nameTextField.getText().isEmpty()) {
            return;
        }
        else{
            tryLogIn();
        }

    }


    public LoginController(){

    }


    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
        this.baseController = (BaseController)parentController;
    }

    private void tryLogIn() {
        String name = nameTextField.getText();
        try {
            switch (loginType) {
                case ADMIN: {
                    //registerAsAdmin(name)
                    break;
                }
                case CUSTOMER: {


                    break;
                }
            }
        }
        catch (Exception ex) {
            parentController.createExceptionDialog(ex);
        }

        baseController.setIsLoggedInProperty(true);
    }


}
