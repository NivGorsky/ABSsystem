package customerScene.information;
import customerScene.CustomerSceneController;
import customerScene.information.accountTransactions.accountTransactionsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import Engine.MainSystem;
import javafx.stage.Stage;
import loansTable.LoansTableComponentController;
import mutualInterfaces.ParentController;


public class InformationController implements ParentController
{
    @FXML private ScrollPane accountTransactions;
    @FXML private accountTransactionsController accountTransactionsController;
    @FXML private Button chargeButton;
    @FXML private TextField amountTextField;
    @FXML private Button withdrawButton;
    @FXML private ScrollPane borrowerLoansTableComponent;
    @FXML private LoansTableComponentController borrowerLoansTableComponentController;
    @FXML private ScrollPane lenderLoansTableComponent;
    @FXML private LoansTableComponentController lenderLoansTableComponentController;

    private CustomerSceneController parentController;
    private StringProperty customerNameProperty;
    private SimpleBooleanProperty isTabSelected;

    public InformationController () {
        customerNameProperty = new SimpleStringProperty();
    }
    public void setParentController(CustomerSceneController parentController) {
        this.parentController = parentController;
    }
    public StringProperty getCustomerNameProperty(){return this.customerNameProperty;}

    @Override
    public MainSystem getModel(){return parentController.getModel();}

    @Override
    public void createExceptionDialog(Exception ex) {
        parentController.createExceptionDialog(ex);
    }

    @Override
    public Stage getPrimaryStage() {
        return parentController.getPrimaryStage();
    }

    @Override
    public void switchStyleSheet(String selectedItem) {
            parentController.switchStyleSheet(selectedItem);
    }

    @Override
    public String getLoggedInUser() {
        return null;
    }


    @FXML
    void chargeButtonClicked(ActionEvent event) {
        String customerName = customerNameProperty.getValue();
        double amount = Double.parseDouble(amountTextField.getText());

        try {
            //make the actual deposit
            amountTextField.clear();
            parentController.getModel().depositMoney(customerName, amount);
            accountTransactionsController.updateAccountMovements();
        }

        catch (Exception ex){
            parentController.createExceptionDialog(ex);
        }
    }

    @FXML
    void withdrawButtonClicked(ActionEvent event) {
        try {
            //need to check regex of only numbers
            if (!amountTextField.getText().isEmpty()){
                String customerName = customerNameProperty.getValue();
                double amount = Double.parseDouble(amountTextField.getText());

                parentController.getModel().withdrawMoney(customerName, amount);
                amountTextField.clear();
                accountTransactionsController.updateAccountMovements();
                amountTextField.clear();
            }
        }

        catch (Exception ex){
            parentController.createExceptionDialog(ex);
        }

        //make the actual withdraw
    }

    @FXML
    public void initialize() {
        try {
            if (accountTransactionsController != null) {
                accountTransactionsController.setParentController(this);
                accountTransactionsController.getCustomerNameProperty().bind(this.customerNameProperty);
                borrowerLoansTableComponentController.setParentController(this);
                lenderLoansTableComponentController.setParentController(this);
            }
        }

        catch (Exception e){
            parentController.createExceptionDialog(new Exception("Failed to init information controller"));
        }
    }

    public void onShow(){
        borrowerLoansTableComponentController.clearTable();
        borrowerLoansTableComponentController.loadSpecificCustomerLoansAsBorrower(customerNameProperty.getValue());
        lenderLoansTableComponentController.clearTable();
        lenderLoansTableComponentController.loadSpecificCustomerLoansAsLender(customerNameProperty.getValue());
        accountTransactionsController.updateAccountMovements();
    }
}
