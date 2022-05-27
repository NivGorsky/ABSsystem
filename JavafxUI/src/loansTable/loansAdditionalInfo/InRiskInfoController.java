package loansTable.loansAdditionalInfo;

import DTO.LoanDTO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import loansTable.LoansTableComponentController;
import mutualInterfaces.ParentController;

public class InRiskInfoController {

    @FXML private ScrollPane inRiskInfo;
    @FXML private TitledPane yazTP;
    @FXML private Label activationYazLabel;
    @FXML private Label nextPaymentLabel;
    @FXML private TitledPane paymentsStatusTP;
    @FXML private Label paidLoanLabel;
    @FXML private Label loanRemainedLabel;
    @FXML private Label paidInterestLabel;
    @FXML private Label InterestRemainedLabel;
    @FXML private TitledPane unpaidPaymentsTP;
    @FXML private TableView<LoanDTO.PaymentDTO> unpaidPaymentsTableView;
    @FXML private TableColumn<LoanDTO.PaymentDTO, Integer> YazCol;
    @FXML private TableColumn<LoanDTO.PaymentDTO, Double> unpaidLoanPartCol;
    @FXML private TableColumn<LoanDTO.PaymentDTO, Double> unpaidInterestPartCol;
    @FXML private TableColumn<LoanDTO.PaymentDTO, Double> totalUnpaidCol;
    @FXML private Label unpaidPaymentsLabel;
    @FXML private Label debtLabel;

    @FXML private TitledPane lendersTP;
    @FXML private TableView<LoanDTO.LenderDetailsDTO> lendersTableView;
    @FXML private LendersTableViewController lendersTableViewController;

    @FXML private TitledPane paidPaymentsTP;
    @FXML private TableView<LoanDTO.PaymentDTO> paidPaymentsTableView;
    @FXML private  PaidPaymentsTableViewController paidPaymentsTableViewController;

    private int activationYAZ;
    private int nextPaymentYAZ;
    private int unpaidPaymentsAmount;
    private double debt;
    private double paidLoan;
    private double remainedLoan;
    private double paidInterest;
    private double remainedInterest;

    private ParentController parentController;

    public void setParentController(ParentController parentController)
    {
        this.parentController = parentController;
    }

    @FXML public void initialize()
    {
        activationYazLabel.textProperty().bind(Bindings.concat("Activated in YAZ: " + activationYAZ));
        nextPaymentLabel.textProperty().bind(Bindings.concat("Next payment in YAZ: " + nextPaymentYAZ));
        unpaidPaymentsLabel.textProperty().bind(Bindings.concat(unpaidPaymentsAmount + " unpaid payments"));
        debtLabel.textProperty().bind(Bindings.concat("Debt: " + debt));
        paidLoanLabel.textProperty().bind(Bindings.concat("Loan already paid: " + paidLoan));
        loanRemainedLabel.textProperty().bind(Bindings.concat("Loan remained to pay: "+ remainedLoan));
        paidInterestLabel.textProperty().bind(Bindings.concat("Interest already paid: " + paidInterest));
        InterestRemainedLabel.textProperty().bind(Bindings.concat("Interest remained to pay: " + remainedInterest));
    }

    public void setData(LoanDTO loan)
    {
        lendersTableViewController.setLendersData(loan);
        paidPaymentsTableViewController.setPaidPaymentsData(loan);
        setUnpaidPayments(loan);

        activationYAZ = loan.getActivationYAZ();
        nextPaymentYAZ = loan.getNextPaymentYAZ();

        paidLoan = loan.getPaidLoan();
        remainedLoan = loan.getInitialAmount() - paidLoan;

        paidInterest = loan.getPaidInterest();
        remainedInterest = loan.getTotalInterest() - paidInterest;
    }




    private void setUnpaidPayments(LoanDTO loan)
    {
        YazCol.setCellValueFactory(cellData -> cellData.getValue().getOriginalYazToPayProperty().asObject());
        unpaidLoanPartCol.setCellValueFactory(cellData -> cellData.getValue().getLoanPaymentProperty().asObject());
        unpaidInterestPartCol.setCellValueFactory(cellData -> cellData.getValue().getInterestPaymentProperty().asObject());
        totalUnpaidCol.setCellValueFactory(cellData -> cellData.getValue().getTotalPaymentProperty().asObject());

        ObservableList<LoanDTO.PaymentDTO> payments = FXCollections.observableArrayList();
        payments.addAll(loan.getUnpaidPayments().values());
        unpaidPaymentsTableView.setItems(payments);

        unpaidPaymentsAmount = loan.getUnpaidPayments().values().size();
        debt = loan.getDebt();
    }

}
