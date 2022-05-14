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
    private MainSystem model;
    private SimpleListProperty<AccountMovementDTO> accountMovements; //need to check where this field will be, understand first how the table view works

    public InformationController () {
        accountMovements = new SimpleListProperty<>();
    }

    private void updateAccountMovements(){
        parentController.updateCustomerDTO();
        CustomerDTO customerDTO = parentController.getCustomerDTO();
        List<AccountMovementDTO> movementsFromEngine = customerDTO.getAccountMovements();
        ObservableListWrapper<AccountMovementDTO> observableMovements = new ObservableListWrapper<>(movementsFromEngine);
        this.accountMovements.setValue(observableMovements); //need to check how to implement this in a table view
    }

    public void setParentController(CustomerController parentController) {
        this.parentController = parentController;
    }

    public void setModel(MainSystem  model){
        this.model = model;
    }

    @FXML private ScrollPane accountTransactions;
    @FXML private accountTransactionsController accountTransactionsController;
    @FXML private Button chargeButton;
    @FXML private TextField amountTextField;
    @FXML private Button withdrawButton;

    @FXML
    void chargeButtonClicked(ActionEvent event) {
        String customerName = parentController.getCustomerNameProperty().getValue();
        double amount = Double.parseDouble(amountTextField.getText());

        try {
            //make the actual deposit
            model.depositMoney(customerName, amount);
            updateAccountMovements();
        }

        catch (Exception e){
            //present an error window to the user
        }
    }

    @FXML
    void withdrawButtonClicked(ActionEvent event) {
        String customerName = parentController.getCustomerNameProperty().getValue();
        double amount = Double.parseDouble(amountTextField.getText());

        //make the actual withdraw
        try {
            model.withdrawMoney(customerName, amount);
            updateAccountMovements();
        }

        catch (Exception e){
            //present an error window to the user
        }
    }

    @FXML
    public void initialize(){
        if (accountTransactionsController != null){
            accountTransactionsController.setParentController(this);
        }

        updateAccountMovements();
    }
}
