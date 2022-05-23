package loansTable.loansAdditionalInfo;

import DTO.LoanDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PaidPaymentsTableViewController {

    @FXML private TableView<LoanDTO.PaymentDTO> paidPaymentsTableView;
    @FXML private TableColumn<LoanDTO.PaymentDTO, Integer> paidInYazCol;
    @FXML private TableColumn<LoanDTO.PaymentDTO, Double> loanPartCol;
    @FXML private TableColumn<LoanDTO.PaymentDTO, Double> interestPartCol;
    @FXML private TableColumn<LoanDTO.PaymentDTO, Double> totalCol;

    public void setPaidPaymentsData(LoanDTO loan)
    {
        paidInYazCol.setCellValueFactory(cellData -> cellData.getValue().getActualPaymentYazProperty().asObject());
        loanPartCol.setCellValueFactory(cellData -> cellData.getValue().getLoanPaymentProperty().asObject());
        interestPartCol.setCellValueFactory(cellData -> cellData.getValue().getInterestPaymentProperty().asObject());
        totalCol.setCellValueFactory(cellData -> cellData.getValue().getTotalPaymentProperty().asObject());

        ObservableList<LoanDTO.PaymentDTO> payments = FXCollections.observableArrayList();
        payments.addAll(loan.getPaidPayments().values());
        paidPaymentsTableView.setItems(payments);
    }
}