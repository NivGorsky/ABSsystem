package customerScene.createLoanScene;

import customerBase.CustomerBaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import mutualInterfaces.BaseController;
import mutualInterfaces.ParentController;

public class CreateLoanSceneController {

    @FXML private ScrollPane createLoan;
    @FXML private TextField loanNameTF;
    @FXML private TextField amountTF;
    @FXML private TextField totalYazToPayTF;
    @FXML private TextField interestPerPaymentTF;
    @FXML private TextField paymentsRateTF;
    @FXML private ComboBox<String> categoriesCB;
    @FXML private Button createLoanButton;

    ParentController parentController;

    @FXML public void initialize() {
        createLoanButton.disableProperty().bind(loanNameTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(amountTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(totalYazToPayTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(interestPerPaymentTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(paymentsRateTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(categoriesCB.selectionModelProperty().isNull());
    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void createLoanButtonClicked() {
        String customerName; //TODO:get from base

        //create http request




    }
}
