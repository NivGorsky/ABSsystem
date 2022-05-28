package loansTable.loansAdditionalInfo;

import DTO.LoanDTO;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import loansTable.LoansTableComponentController;

public class PendingInfoController {

    @FXML private ScrollPane pendingInfo;
    @FXML private TitledPane lendersTP;
    @FXML private Label raisedAmountLabel;
    @FXML private Label amountToRaiseLabel;

    @FXML private TableView<LoanDTO.LenderDetailsDTO> lendersTableView;
    @FXML private LendersTableViewController lendersTableViewController;

    private double raisedAmount;
    private double amountToRaise;

    private LoansTableComponentController parentController;

    public void setParentController(LoansTableComponentController parentController) {
        this.parentController = parentController;
    }

    public void setData(LoanDTO loan)
    {
        lendersTableViewController.setLendersData(loan);

        this.raisedAmount = loan.getAmountRaised();
        this.amountToRaise = (loan.getInitialAmount() - this.raisedAmount);

        raisedAmountLabel.setText("Amount raised so far: " + this.raisedAmount);
        amountToRaiseLabel.setText("Amount remained to raise: " + this.amountToRaise);
    }
}
