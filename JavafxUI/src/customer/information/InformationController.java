package customer.information;
import customer.CustomerController;
import customer.information.accountTransactions.accountTransactionsController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import Engine.MainSystem;
import Engine.ABSsystem;
import DTO.*;

import java.util.ArrayList;


public class InformationController {

    private CustomerController parentController;
    private MainSystem model;
    private ListProperty<AccountMovementDTO> accountMovements;

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
        String customerName = parentController.getCustomerNameProperty().getValue();
        double amount = Double.parseDouble(amountTextField.getText());

        model.depositMoney(customerName, amount);




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

    public InformationController (){

    }

    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }
    public void setModel(MainSystem model){
        this.model = model;
    }

}
