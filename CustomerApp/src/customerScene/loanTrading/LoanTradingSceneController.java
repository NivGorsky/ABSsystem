package customerScene.loanTrading;

import DTO.LoanDTO;
import DTO.LoanForSaleDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import loansTable.LoansTableComponentController;
import mutualInterfaces.ParentController;

public class LoanTradingSceneController {

    @FXML private ScrollPane loansTrade;
    @FXML private AnchorPane loansForSaleAP;
    @FXML private Button buyLoanButton;
    @FXML private AnchorPane loansAsLenderAP;
    @FXML private Button sellLoanButton;
    @FXML private Label loanPriceLabel;
    @FXML private Label sellerLabel;

    @FXML private ScrollPane loansTableComponent;
    @FXML private LoansTableComponentController loansTableComponentController;

    private LoanDTO selectedLoanFromLoansTable;
    private SimpleBooleanProperty isLoanSelected;
    private SimpleDoubleProperty loanPrice;
    private SimpleStringProperty seller;

    private ParentController parentController;

    public LoanTradingSceneController() {
        selectedLoanFromLoansTable = null;
        isLoanSelected = new SimpleBooleanProperty(false);
        loanPrice = new SimpleDoubleProperty();
        seller = new SimpleStringProperty();
    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }

    @FXML public void initialize() {
        loanPriceLabel.visibleProperty().bind(isLoanSelected);
        loanPriceLabel.textProperty().bind(Bindings.concat("Loan Price: " , loanPrice.getValue()));

        sellerLabel.visibleProperty().bind(isLoanSelected);
        sellerLabel.textProperty().bind(Bindings.concat("Seller: " , seller.getValue()));
    }

    @FXML
    void buyLoanButtonClicked(ActionEvent event) {
      //  LoanForSaleDTO loanToBuy = new LoanForSaleDTO(parentController.getLoggedInUser(), )
    }

    @FXML
    void sellLoanButtonClicked(ActionEvent event) {

    }

}
