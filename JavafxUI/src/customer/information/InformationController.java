package customer.information;
import customer.CustomerController;
import customer.information.accountTransactions.accountTransactionsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import Engine.MainSystem;
import loansTable.LoansTableComponentController;
import mutualInterfaces.ParentController;


public class InformationController implements ParentController {

    private CustomerController parentController;
    private StringProperty customerNameProperty;
    private SimpleBooleanProperty isTabSelected;

    public InformationController () {
        customerNameProperty = new SimpleStringProperty();
    }
    public void setParentController(CustomerController parentController) {
        this.parentController = parentController;
    }
    public StringProperty getCustomerNameProperty(){return this.customerNameProperty;}

    @Override
    public MainSystem getModel(){return parentController.getModel();}

    @Override
    public void createExceptionDialog(Exception ex) {
        parentController.createExceptionDialog(ex);
    }



    @FXML private ScrollPane accountTransactions;
    @FXML private accountTransactionsController accountTransactionsController;
    @FXML private Button chargeButton;
    @FXML private TextField amountTextField;
    @FXML private Button withdrawButton;
    @FXML private ScrollPane borrowerLoansTableComponent;
    @FXML private LoansTableComponentController borrowerLoansTableComponentController;
    @FXML private ScrollPane lenderLoansTableComponent;
    @FXML private LoansTableComponentController lenderLoansTableComponentController;


    @FXML
    void chargeButtonClicked(ActionEvent event) {
        String customerName = customerNameProperty.getValue();
        double amount = Double.parseDouble(amountTextField.getText());

        try {
            //make the actual deposit
            parentController.getModel().depositMoney(customerName, amount);
            accountTransactionsController.updateAccountMovements();
        }

        catch (Exception e){
            //present an error window to the user
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
                accountTransactionsController.updateAccountMovements();
            }
        }

        catch (Exception e){

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
            System.out.println("Failed to init information controller");
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
