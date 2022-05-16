package loansTable;

import DTO.LoanDTO;
import adminScene.AdminSceneController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LoansTableComponentController {

    @FXML private TableView<LoanDTO> loansTable;
    @FXML private TableColumn<LoanDTO, String> loanNameCol;
    @FXML private TableColumn<LoanDTO, String> loanerNameCol;
    @FXML private TableColumn<LoanDTO, String> categoryCol;
    @FXML private TableColumn<LoanDTO, Double> initialAmountCol;
    @FXML private TableColumn<LoanDTO, Integer> totalYazCol;
    @FXML private TableColumn<LoanDTO, Double> totalInterestCol;
    @FXML private TableColumn<LoanDTO, Integer> PaymentRateCol;
    @FXML private TableColumn<LoanDTO, String> statusCol;

    private AdminSceneController parentController;

    @FXML public void initialize()
    {
//        loanNameCol.setCellValueFactory(new PropertyValueFactory<>("loanName"));
//        loanerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
//        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
//        initialAmountCol.setCellValueFactory(new PropertyValueFactory<>("initialAmount"));
//        totalYazCol.setCellValueFactory(new PropertyValueFactory<>("maxYazToPay"));
//        totalInterestCol.setCellValueFactory(new PropertyValueFactory<>("totalInterest"));
//        PaymentRateCol.setCellValueFactory(new PropertyValueFactory<>("yazPerPayment"));
//        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void setParentController(AdminSceneController adminSceneController)
    {
        parentController = adminSceneController;
    }

//    public void loadLoansData()
//    {
//        ObservableList<LoanDTO> loans =
//        loansTable.setItems(loans);
//    }
}
