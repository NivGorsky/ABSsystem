package customer.information;
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
    private SimpleListProperty<AccountMovementDTO> accountMovements;
    private SimpleStringProperty customerName;

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

        //make the actual deposit
        model.depositMoney(customerName, amount);

        //update the movements table
        CustomerDTO customerDTO = model.getCustomerDTO(customerName);
        List<AccountMovementDTO> movementsFromEngine = customerDTO.getAccountMovements();
        ObservableListWrapper<AccountMovementDTO> observableMovements = new ObservableListWrapper<>(movementsFromEngine);
        this.accountMovements.setValue(observableMovements);
    }

    @FXML
    void withdrawButtonClicked(ActionEvent event) {

    }

    @FXML
    public void initialize(){
        if (accountTransactionsController != null){
            accountTransactionsController.setParentController(this);
        }
        
        //update customer name
        updateAccountMovements();
    }

    public InformationController () {
        accountMovements = new SimpleListProperty<>();
    }

    private void updateAccountMovements(){
        CustomerDTO customerDTO = model.getCustomerDTO(customerName.get());
        List<AccountMovementDTO> movementsFromEngine = customerDTO.getAccountMovements();
        ObservableListWrapper<AccountMovementDTO> observableMovements = new ObservableListWrapper<>(movementsFromEngine);
        this.accountMovements.setValue(observableMovements);
    }

}
