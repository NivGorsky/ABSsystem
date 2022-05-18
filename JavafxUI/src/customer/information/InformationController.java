package customer.information;
import Engine.SystemService;
import com.sun.javafx.collections.ObservableListWrapper;
import customer.CustomerController;
import customer.information.accountTransactions.accountTransactionsController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import Engine.MainSystem;
import Engine.ABSsystem;
import DTO.*;


import java.util.ArrayList;
import java.util.List;


public class InformationController {

    private CustomerController parentController;
    private StringProperty customerNameProperty;
    private MainSystem model;

    public InformationController () {
        customerNameProperty = new SimpleStringProperty();
    }

    public void setParentController(CustomerController parentController) {
        this.parentController = parentController;
    }
    public StringProperty getCustomerNameProperty(){return this.customerNameProperty;}
    public void setModel(MainSystem  model){
        this.model = model;
        accountTransactionsController.setModel(model);
    }

    @FXML private ScrollPane accountTransactions;
    @FXML private accountTransactionsController accountTransactionsController;
    @FXML private Button chargeButton;
    @FXML private TextField amountTextField;
    @FXML private Button withdrawButton;

    @FXML
    void chargeButtonClicked(ActionEvent event) {
        String customerName = customerNameProperty.getValue();
        double amount = Double.parseDouble(amountTextField.getText());

        try {
            //make the actual deposit
            model.depositMoney(customerName, amount);
            accountTransactionsController.updateAccountMovements();
        }

        catch (Exception e){
            //present an error window to the user
        }
    }

    @FXML
    void withdrawButtonClicked(ActionEvent event) {
        try {
            //need to check regex of only numbers
            if (!amountTextField.getText().isEmpty()){
                String customerName = customerNameProperty.getValue();
                double amount = Double.parseDouble(amountTextField.getText());

                model.withdrawMoney(customerName, amount);
                accountTransactionsController.updateAccountMovements();
            }
        }

        catch (Exception e){
            //present an error window to the user
        }

        //make the actual withdraw
    }

    @FXML
    public void initialize(){
        try {
            if (accountTransactionsController != null) {
                accountTransactionsController.setParentController(this);
                accountTransactionsController.getCustomerNameProperty().bind(this.customerNameProperty);
                //accountTransactionsController.updateAccountMovements();
            }
        }

        catch (Exception e){
            System.out.println("Failed to init information controller");
        }
    }
}
