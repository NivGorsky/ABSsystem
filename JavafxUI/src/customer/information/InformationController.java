package customer.information;
import customer.CustomerController;
import customer.information.accountTransactions.accountTransactionsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.*;

public class InformationController {

    private CustomerController parentController;

    @FXML private ScrollPane accountTransactionsComponent;
    @FXML private accountTransactionsController accountTransactionsComponentController;

    @FXML
    private Button chargeButton;

    @FXML
    private TextField amountTextField;

    @FXML
    private Button withdrawButton;

    @FXML
    void chargeButtonClicked(ActionEvent event) {

    }

    @FXML
    void withdrawButtonClicked(ActionEvent event) {

    }

    @FXML
    public void initialize(){
        if (accountTransactionsComponentController != null){
            accountTransactionsComponentController.setParentController(this);
        }
    }

    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }


}
