package loansTable;

import DTO.LoanDTO;
import adminScene.AdminSceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mutualInterfaces.ParentController;

import java.util.ArrayList;

public class LoansTableComponentController {

    @FXML private TableView<LoanDTO> loansTable;
    @FXML private TableColumn<LoanDTO, String> loanNameCol;
    @FXML private TableColumn<LoanDTO, String> loanerNameCol;
    @FXML private TableColumn<LoanDTO, String> categoryCol;
    @FXML private TableColumn<LoanDTO, Double> initialAmountCol;
    @FXML private TableColumn<LoanDTO, Integer> totalYazCol;
    @FXML private TableColumn<LoanDTO, Double> totalInterestCol;
    @FXML private TableColumn<LoanDTO, Integer> PaymentRateCol;
    @FXML private TableColumn<LoanDTO, String> statusCol;

    private ParentController parentController;

    public void setParentController(ParentController adminSceneController) {
        parentController = adminSceneController;
    }

    public void loadLoansData()
    {
        createTableLoanColumns();
        ArrayList<LoanDTO> loans = parentController.getModel().showLoansInfo();
        putLoansInTable(loans);
    }

    public void loadSpecificCustomerLoansAsLender(String customerName){
        createTableLoanColumns();
        ArrayList<LoanDTO> loans = parentController.getModel().getLoansByCustomerNameAsLender(customerName);
        putLoansInTable(loans);
    }

    public void loadSpecificCustomerLoansAsBorrower(String customerName){
        createTableLoanColumns();
        ArrayList<LoanDTO> loans = parentController.getModel().getLoansByCustomerNameAsBorrower(customerName);
        putLoansInTable(loans);
    }

    private void putLoansInTable( ArrayList<LoanDTO> loans){
        ObservableList<LoanDTO> loansForTable = FXCollections.observableArrayList();
        loansForTable.addAll(loans);
        loansTable.setItems(loansForTable);
    }

    public void createTableLoanColumns(){
        loanNameCol.setCellValueFactory(cellData -> cellData.getValue().getLoanNameProperty());
        loanerNameCol.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());
        initialAmountCol.setCellValueFactory(cellData -> cellData.getValue().getInitialAmountProperty().asObject());
        totalYazCol.setCellValueFactory(cellData -> cellData.getValue().getMaxYazToPayProperty().asObject());
        totalInterestCol.setCellValueFactory(cellData -> cellData.getValue().getTotalInterestProperty().asObject());
        PaymentRateCol.setCellValueFactory(cellData -> cellData.getValue().getYazPerPaymentProperty().asObject());
        statusCol.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());
    }
}
