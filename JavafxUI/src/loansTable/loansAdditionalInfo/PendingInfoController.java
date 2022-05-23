package loansTable.loansAdditionalInfo;

import DTO.LoanDTO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import loansTable.LoansTableComponentController;

import java.beans.BeanInfo;

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

    public void setParentController(LoansTableComponentController parentController)
    {
        this.parentController = parentController;
    }

    @FXML public void initialize()
    {
        raisedAmountLabel.textProperty().bind(Bindings.concat("Amount raised so far: " + raisedAmount));
        amountToRaiseLabel.textProperty().bind(Bindings.concat("Amount remained to raise: " + amountToRaise));
    }

    public void setData(LoanDTO loan)
    {
        lendersTableViewController.setLendersData(loan);

        raisedAmount = loan.getAmountRaised();
        amountToRaise = loan.getInitialAmount() - raisedAmount;
    }
}
