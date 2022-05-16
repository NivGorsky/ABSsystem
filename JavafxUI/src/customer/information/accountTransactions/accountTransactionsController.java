package customer.information.accountTransactions;


import DTO.*;

import Engine.ABSsystem;
import Engine.MainSystem;
import com.sun.javafx.collections.ObservableListWrapper;
import customer.information.InformationController;
import javafx.beans.property.StringProperty;
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

    public void setParentController(InformationController parentController){
        this.parentController = parentController;
    }

    public void setModel(MainSystem model){
        this.model = model;
    }

    public StringProperty getCustomerNameProperty(){return this.customerNameProperty;}

    public void updateAccountMovements(){
        CustomerDTO customerDTO = model.getCustomerDTO(customerNameProperty.getValue());
        List<AccountMovementDTO> movementsFromEngine = customerDTO.getAccountMovements();
        ObservableListWrapper<AccountMovementDTO> observableMovements = new ObservableListWrapper<>(movementsFromEngine);
        accountMovements.clear();
        accountMovements = observableMovements;
        accountTransactionsTableView.setItems(accountMovements); //should be as listening to property
    }

    public accountTransactionsController(){
        this.accountMovements = FXCollections.observableArrayList();
    }

    private void createTableViewColumns(){
        TableColumn<AccountMovementDTO, Integer> yazCol = new TableColumn<>("Yaz");
        yazCol.setCellValueFactory(new PropertyValueFactory<>("yaz"));
        accountTransactionsTableView.getColumns().add(yazCol);

        TableColumn<AccountMovementDTO, Integer> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        accountTransactionsTableView.getColumns().add(amountCol);

        TableColumn<AccountMovementDTO, Integer> movementKindCol = new TableColumn<>("Movement Kind");
        movementKindCol.setCellValueFactory(new PropertyValueFactory<>("movementKind"));
        accountTransactionsTableView.getColumns().add(movementKindCol);

        TableColumn<AccountMovementDTO, Integer> balanceBeforeCol = new TableColumn<>("Balance Before");
        balanceBeforeCol.setCellValueFactory(new PropertyValueFactory<>("balanceBefore"));
        accountTransactionsTableView.getColumns().add(balanceBeforeCol);

        TableColumn<AccountMovementDTO, Integer> balanceAfterCol = new TableColumn<>("Balance After");
        movementKindCol.setCellValueFactory(new PropertyValueFactory<>("balanceAfter"));
        accountTransactionsTableView.getColumns().add(balanceAfterCol);
    }

    @FXML
    public void initialize(){
        try {
            createTableViewColumns();
            updateAccountMovements();
        }

        catch (Exception e){
            System.out.println("Failed to init account transactions controller");
        }
    }

    @FXML private TableView<AccountMovementDTO> accountTransactionsTableView;
}
