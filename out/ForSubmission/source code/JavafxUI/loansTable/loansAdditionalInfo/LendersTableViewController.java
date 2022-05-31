package loansTable.loansAdditionalInfo;

import DTO.LoanDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LendersTableViewController {
    @FXML private TableView<LoanDTO.LenderDetailsDTO> lendersTableView;
    @FXML private TableColumn<LoanDTO.LenderDetailsDTO, String> nameCol;
    @FXML private TableColumn<LoanDTO.LenderDetailsDTO, Double> amountCol;

    public void setLendersData(LoanDTO loan)
    {
        nameCol.setCellValueFactory(cellData -> cellData.getValue().lenderNameProperty());
        amountCol.setCellValueFactory(cellData -> cellData.getValue().lendersInvestAmountProperty().asObject());

        ObservableList<LoanDTO.LenderDetailsDTO> lenders = FXCollections.observableArrayList();
        lenders.addAll(loan.getLenderDTOS());
        lendersTableView.setItems(lenders);
    }
}
