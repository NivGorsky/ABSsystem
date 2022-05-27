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

    @FXML public void initialize()
    {
        paidLoanLabel.textProperty().bind(Bindings.concat("Loan already paid: " + paidLoan));
        loanRemainedLabel.textProperty().bind(Bindings.concat("Loan remained to pay: "+ remainedLoan));
        paidInterestLabel.textProperty().bind(Bindings.concat("Interest already paid: " + paidInterest));
        InterestRemainedLabel.textProperty().bind(Bindings.concat("Interest remained to pay: " + remainedInterest));
    }

    public void setData(LoanDTO loan)
    {
        lendersTableViewController.setLendersData(loan);
        paidPaymentsTableViewController.setPaidPaymentsData(loan);

        paidLoan = loan.getPaidLoan();
        remainedLoan = loan.getInitialAmount() - paidLoan;

        paidInterest = loan.getPaidInterest();
        remainedInterest = loan.getTotalInterest() - paidInterest;
    }



}
