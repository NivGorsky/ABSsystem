package loansTable.loansAdditionalInfo;

import DTO.LoanDTO;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import mutualInterfaces.ParentController;

public class ActiveInfoController {

    @FXML private ScrollPane activeInfo;

    @FXML private TitledPane yazTP;
    @FXML private Label activationYazLabel;
    @FXML private Label nextPaymentLabel;

    @FXML private TitledPane paymentsStatusTP;
    @FXML private Label paidLoanLabel;
    @FXML private Label loanRemainedLabel;
    @FXML private Label paidInterestLabel;
    @FXML private Label InterestRemainedLabel;

    @FXML private TitledPane lendersTP;
    @FXML private TableView<LoanDTO.LenderDetailsDTO> lendersTableView;
    @FXML private LendersTableViewController lendersTableViewController;

    @FXML private TitledPane PaymentsTP;
    @FXML private TableView<LoanDTO.PaymentDTO> paidPaymentsTableView;
    @FXML private PaidPaymentsTableViewController paidPaymentsTableViewController;

    private double paidLoan;
    private double remainedLoan;
    private double paidInterest;
    private double remainedInterest;

    private ParentController parentController;

    public void setParentController(ParentController parentController)
    {
        this.parentController = parentController;
    }
    
    public void setData(LoanDTO loan)
    {
       lendersTableViewController.setLendersData(loan);
       paidPaymentsTableViewController.setPaidPaymentsData(loan);

       paidLoan = loan.getPaidLoan();
       paidLoanLabel.setText("Loan already paid: " + paidLoan);

       remainedLoan = loan.getInitialAmount() - paidLoan;
       loanRemainedLabel.setText("Loan remained to pay: "+ remainedLoan);

       paidInterest = loan.getPaidInterest();
       paidInterestLabel.setText("Interest already paid: " + paidInterest);

       remainedInterest = loan.getTotalInterest() - paidInterest;
       InterestRemainedLabel.setText("Interest remained to pay: " + remainedInterest);
    }
}
