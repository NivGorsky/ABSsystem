package generalScenes;

import com.sun.xml.internal.ws.api.pipe.Engine;
import Engine.Loan;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LoansTableSceneController {

    @FXML private TableView<Loan> loansTable;
    @FXML private TableColumn<Loan, String> loanName;
    @FXML private TableColumn<Loan, String> loanerName;
    @FXML private TableColumn<Loan, String> category;
    @FXML private TableColumn<Loan, Double> initialAmount;
    @FXML private TableColumn<Loan, Integer> totalYaz;
    @FXML private TableColumn<Loan, Double> totalInterest;
    @FXML private TableColumn<Loan, Integer> PaymentRate;
    @FXML private TableColumn<Loan, Engine.Loan.LoanStatus> status;



}
