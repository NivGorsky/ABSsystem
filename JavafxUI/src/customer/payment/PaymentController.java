package customer.payment;

import customer.CustomerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

public class PaymentController {

    private CustomerController parentController;

    @FXML
    private Accordion notificationsBoard;

    @FXML
    private Button payToLenderButton;

    @FXML
    private Button payToAllLendersButton;

    @FXML
    private Button closeLoanButton;

    @FXML
    private TableColumn<?, ?> lendersTableView;

    @FXML
    void closeLoanButtonClicked(ActionEvent event) {

    }

    @FXML
    void payToAllLendersButtonClicked(ActionEvent event) {

    }

    @FXML
    void payToLenderButtonClicked(ActionEvent event) {

    }

    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }

}
