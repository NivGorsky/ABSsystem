package customer.loanTrading;

import DTO.LoanDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import loansTable.LoansTableComponentController;
import mutualInterfaces.ParentController;

public class LoanTradingController {

    @FXML private ScrollPane loansTrade;
    @FXML private AnchorPane loansForSaleAP;
    @FXML private Button buyLoanButton;
    @FXML private AnchorPane loansAsLenderAP;
    @FXML private Button sellLoanButton;
    @FXML private Label loanPriceLabel;

    @FXML private ScrollPane loansTableComponent;
    @FXML private LoansTableComponentController loansTableComponentController;

    private LoanDTO selectedLoanFromLoansTable;
    private SimpleBooleanProperty isLoanSelected;
    private double loanPrice;

    private ParentController parentController;

    public LoanTradingController () {
        selectedLoanFromLoansTable = null;
        isLoanSelected = new SimpleBooleanProperty(false);
    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }

    @FXML public void initialize() {
        loanPriceLabel.disableProperty().bind(isLoanSelected.not());
        loanPriceLabel.textProperty().bind(Bindings.concat("Loan Price: " , loanPrice));
    }

    @FXML
    void buyLoanButtonClicked(ActionEvent event) {

    }

    @FXML
    void sellLoanButtonClicked(ActionEvent event) {

    }

}
