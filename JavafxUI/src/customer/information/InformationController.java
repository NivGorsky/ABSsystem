package customer.information;
import customer.CustomerController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InformationController {

    private CustomerController parentController;

    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }

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

}
