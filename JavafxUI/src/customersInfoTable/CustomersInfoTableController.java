package customersInfoTable;

import DTO.CustomerDTO;
import DTO.LoanDTO;
import Engine.Customer;
import adminScene.AdminSceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class CustomersInfoTableController {

    @FXML private TableView<CustomerDTO> customersInfoTable;
    @FXML private TableColumn<CustomerDTO, String> customerNameColumn;
    @FXML private TableColumn<CustomerDTO, Integer> loansAsLenderColumn;
    @FXML private TableColumn<CustomerDTO, Integer> newLoansColumn;
    @FXML private TableColumn<CustomerDTO, Integer> pendingLoansColumn;
    @FXML private TableColumn<CustomerDTO, Integer> activeLoansColumn;
    @FXML private TableColumn<CustomerDTO, Integer> loansInRiskColumn;
    @FXML private TableColumn<CustomerDTO, Integer> finishedLoansColumn;
    @FXML private TableColumn<CustomerDTO, Integer> loansAsLoanerColumn;
    @FXML private TableColumn<CustomerDTO, Integer> newLoansLoanerColumn;
    @FXML private TableColumn<CustomerDTO, Integer> pendingLoansLoanerColumn;
    @FXML private TableColumn<CustomerDTO, Integer> activeLoansLoanerColumn;
    @FXML private TableColumn<CustomerDTO, Integer> loansInRiskLoanerColumn;
    @FXML private TableColumn<CustomerDTO, Integer> finishedLoansLoanerColumn;

    AdminSceneController parentController;

    public void setParentController(AdminSceneController adminSceneController)
    {
        parentController = adminSceneController;
    }

    public void loadCustomersInfo()
    {
        customerNameColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
        loansAsLenderColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLenderLoansProperty().asObject());
        loansAsLoanerColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoanerLoansProperty().asObject());

        newLoansColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLender("NEW").asObject());
        pendingLoansColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLender("PENDING").asObject());
        activeLoansColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLender("ACTIVE").asObject());
        loansInRiskColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLender("IN_RISK").asObject());
        finishedLoansColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLender("FINISHED").asObject());

        newLoansLoanerColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLoaner("NEW").asObject());
        pendingLoansLoanerColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLoaner("PENDING").asObject());
        activeLoansLoanerColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLoaner("ACTIVE").asObject());
        loansInRiskLoanerColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLoaner("IN_RISK").asObject());
        finishedLoansLoanerColumn.setCellValueFactory(cellData -> cellData.getValue().getAmountOfLoansPropertyPerStatusAsLoaner("FINISHED").asObject());

        ArrayList<CustomerDTO> customers = parentController.getModel().showCustomersInfo();
        ObservableList<CustomerDTO> customersForTable = FXCollections.observableArrayList();
        customersForTable.addAll(customers);
        customersInfoTable.setItems(customersForTable);
    }
}
