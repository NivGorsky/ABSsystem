package customer.information;
import customer.CustomerController;
import customer.information.accountTransactions.accountTransactionsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;


public class InformationController {

    private CustomerController parentController;

    @FXML private ScrollPane accountTransactions;
    @FXML private accountTransactionsController accountTransactionsController;

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
        if (accountTransactionsController != null){
            accountTransactionsController.setParentController(this);
        }
    }

    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }


}
