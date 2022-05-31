package loansTable.loansAdditionalInfo;

import DTO.LoanDTO;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import loansTable.LoansTableComponentController;
import loansTable.loansAdditionalInfo.LendersTableViewController;
import loansTable.loansAdditionalInfo.PaidPaymentsTableViewController;
import mutualInterfaces.ParentController;

public class FinishedInfoController {

    @FXML private TitledPane lendersTP;
    @FXML private TableView<LoanDTO.LenderDetailsDTO> lendersTableView;
    @FXML private LendersTableViewController lendersTableViewController;

    @FXML private TitledPane paymentsTP;
    @FXML private TableView<LoanDTO.PaymentDTO> paidPaymentsTableView;
    @FXML private PaidPaymentsTableViewController paidPaymentsTableViewController;

    @FXML private TitledPane yazInfoTP;
    @FXML private Label startYazLabel;
    @FXML private Label endYAZLabel;

    private int startYAZ;
    private int endYAZ;

    private ParentController parentController;

    public void setParentController(ParentController parentController)
    {
        this.parentController = parentController;
    }

    @FXML public void initialize()
    {
        startYazLabel.textProperty().bind(Bindings.concat("Started in YAZ: " + startYAZ));
        endYAZLabel.textProperty().bind(Bindings.concat("Ended in YAZ: " + endYAZ));
    }

    public void setData(LoanDTO loan)
    {
        lendersTableViewController.setLendersData(loan);
        paidPaymentsTableViewController.setPaidPaymentsData(loan);
        startYAZ = loan.getActivationYAZ();
        endYAZ = loan.getFinishYAZ();
        startYazLabel.textProperty().bind(Bindings.concat("Started in YAZ: " + startYAZ));
        endYAZLabel.textProperty().bind(Bindings.concat("Ended in YAZ: " + endYAZ));
    }

}
