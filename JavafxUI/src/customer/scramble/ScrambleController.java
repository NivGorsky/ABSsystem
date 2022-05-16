package customer.scramble;

import customer.CustomerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ScrambleController {

    private CustomerController parentController;

    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }

    @FXML
    private RadioButton minInterestRatioButton;

    @FXML
    private TextField minInterestTextField;

    @FXML
    private TextField maxPercentageOwnershipTextField;

    @FXML
    private TextField minYazTextField;

    @FXML
    private TextField maxOpenLoansTextField;

    @FXML
    private RadioButton minYazRadioButton;

    @FXML
    private RadioButton maxOpenLoansRadioButton;

    @FXML
    private RadioButton maxPercentageOwnershipRadioButton;

    @FXML
    private RadioButton amountRadioButton;

    @FXML
    private TextField amountTextField;

    @FXML
    private RadioButton categoriesRadioButton;

    @FXML
    private TableView<?> loanCategoriesViewTable;

    @FXML
    private Button findLoansButton;

    @FXML
    private ProgressBar findLoansProgressionBar;

    @FXML
    void categoriesRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    void findLoansButtonClicked(ActionEvent event) {

    }

    @FXML
    void loanCategoriesViewTableSortClicked(ActionEvent event) {

    }

    @FXML
    void maxOpenLoansRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    void maxPercentageOwnershipRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    void minInterestRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    void minYazRadioButtonClicked(ActionEvent event) {

    }

}
