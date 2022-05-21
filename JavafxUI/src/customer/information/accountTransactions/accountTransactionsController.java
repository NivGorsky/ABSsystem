package customer.information.accountTransactions;


import DTO.*;

import Engine.ABSsystem;
import Engine.MainSystem;
import com.sun.javafx.collections.ObservableListWrapper;
import customer.information.InformationController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class accountTransactionsController {

    //TODO: create the link between this controller's customer name property to the customer controller customer name property.

    private InformationController parentController;
    private MainSystem model;
    private StringProperty customerNameProperty;
    private ObservableList<AccountMovementDTO> accountMovements;
    private boolean isModelLoaded;//only if it;s proiperty

    public void setParentController(InformationController parentController){
        this.parentController = parentController;
    }

    public void setModel(MainSystem model){
        this.model = model;
        this.isModelLoaded = true;
    }

    public StringProperty getCustomerNameProperty(){return this.customerNameProperty;}

    public void updateAccountMovements(){
        CustomerDTO customerDTO = model.getCustomerDTO(customerNameProperty.getValue());
        List<AccountMovementDTO> movementsFromEngine = customerDTO.getAccountMovements();
        accountMovements.clear();
        accountMovements.addAll(movementsFromEngine);
        accountTransactionsTableView.setItems(accountMovements); //should be as listening to property
    }

    public accountTransactionsController(){
        accountMovements = FXCollections.observableArrayList();
        customerNameProperty = new SimpleStringProperty();
        isModelLoaded = false;
    }

    private void createTableViewColumns(){
        TableColumn<AccountMovementDTO, Integer> yazCol = new TableColumn<AccountMovementDTO, Integer>("Yaz");
        yazCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Integer>("yaz"));
        accountTransactionsTableView.getColumns().add(yazCol);

        TableColumn<AccountMovementDTO, Double> amountCol = new TableColumn<AccountMovementDTO, Double>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Double>("amount"));
        accountTransactionsTableView.getColumns().add(amountCol);

        TableColumn<AccountMovementDTO, Character> movementKindCol = new TableColumn<AccountMovementDTO, Character>("Movement Kind");
        movementKindCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Character>("movementKind"));
        accountTransactionsTableView.getColumns().add(movementKindCol);

        TableColumn<AccountMovementDTO, Double> balanceBeforeCol = new TableColumn<AccountMovementDTO, Double>("Balance Before");
        balanceBeforeCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Double>("balanceBefore"));
        accountTransactionsTableView.getColumns().add(balanceBeforeCol);

        TableColumn<AccountMovementDTO, Double> balanceAfterCol = new TableColumn<AccountMovementDTO, Double>("Balance After");
        balanceAfterCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Double>("balanceAfter"));
        accountTransactionsTableView.getColumns().add(balanceAfterCol);
    }

    @FXML
    public void initialize(){
        try {
            createTableViewColumns();
            customerNameProperty.addListener((observable, oldValue, newValue) -> {
//                updateAccountMovements();
            });
        }

        catch (Exception e){
            System.out.println("Failed to init account transactions controller");
        }
    }

    @FXML private TableView<AccountMovementDTO> accountTransactionsTableView;
}
