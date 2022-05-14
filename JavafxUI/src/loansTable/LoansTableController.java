package loansTable;

import DTO.LoanDTO;
import Engine.Loan;
import adminScene.AdminSceneController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class LoansTableController {

    @FXML private TableView<LoanDTO> loansTable;
    @FXML private TableColumn<LoanDTO, String> loanName;
    @FXML private TableColumn<LoanDTO, String> loanerName;
    @FXML private TableColumn<LoanDTO, String> category;
    @FXML private TableColumn<LoanDTO, Double> initialAmount;
    @FXML private TableColumn<LoanDTO, Integer> totalYaz;
    @FXML private TableColumn<LoanDTO, Double> totalInterest;
    @FXML private TableColumn<LoanDTO, Integer> PaymentRate;
    @FXML private TableColumn<LoanDTO, String> status;

    private AdminSceneController parentController;


    public void setTableItems()
    {
        ObservableList<LoanDTO> loans =  (ObservableList<LoanDTO>)parentController.getModel().showLoansInfo();
        loansTable.setItems(loans);
    }



}
