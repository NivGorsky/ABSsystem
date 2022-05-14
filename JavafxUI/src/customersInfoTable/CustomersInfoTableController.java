package customersInfoTable;

import Engine.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CustomersInfoTableController {

    @FXML private TableView<Customer> customersInfoTable;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, Integer> loansAsLenderColumn;
    @FXML private TableColumn<Customer, Integer> newLoansColumn;
    @FXML private TableColumn<Customer, Integer> pendingLoansColumn;
    @FXML private TableColumn<Customer, Integer> activeLoansColumn;
    @FXML private TableColumn<Customer, Integer> loansInRiskColumn;
    @FXML private TableColumn<Customer, Integer> finishedLoansColumn;
    @FXML private TableColumn<Customer, Integer> loansAsLoanerColumn;
    @FXML private TableColumn<Customer, Integer> newLoansLoanerColumn;
    @FXML private TableColumn<Customer, Integer> pendingLoansLoanerColumn;
    @FXML private TableColumn<Customer, Integer> activeLoansLoanerColumn;
    @FXML private TableColumn<Customer, Integer> loansInRiskLoanerColumn;
    @FXML private TableColumn<Customer, Integer> finishedLoansLoanerColumn;

}
