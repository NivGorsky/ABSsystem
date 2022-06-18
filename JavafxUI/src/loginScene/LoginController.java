package loginScene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import mutualInterfaces.ParentController;

public class LoginController {

    @FXML private ScrollPane login;
    @FXML private Label loginLabel;
    @FXML private Label nameLabel;
    @FXML private TextField nameTextField;
    @FXML private Button OKButton;

    private ParentController parentController;

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }
}
