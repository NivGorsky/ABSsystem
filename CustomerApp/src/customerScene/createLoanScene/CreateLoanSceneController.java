package customerScene.createLoanScene;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import mutualInterfaces.ParentController;

public class CreateLoanSceneController {

    @FXML private ScrollPane createLoan;
    @FXML private TextField loanNameTF;
    @FXML private TextField loanerNameTF1;
    @FXML private TextField amountTF;
    @FXML private TextField totalYazToPayTF;
    @FXML private TextField interestPerPaymentTF;
    @FXML private TextField paymentsRateTF;
    @FXML private ComboBox<?> categoriesCB;

    ParentController parentController;

    @FXML public void initialize() {

    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }
}
