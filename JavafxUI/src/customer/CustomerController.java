package customer;
import Engine.MainSystem;
import customer.information.InformationController;
import customer.payment.PaymentController;
import customer.scramble.ScrambleController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import mainScene.MainSceneController;
import mutualInterfaces.ParentController;


public class CustomerController implements ParentController {

    private MainSceneController parentController;
    private StringProperty customerNameProperty;

    public CustomerController(){
        customerNameProperty = new SimpleStringProperty();
    }

    private boolean areControllersInitialized(InformationController customerController, ScrambleController scrambleController, PaymentController paymentController){

        return customerController != null && scrambleController != null && paymentController != null;
    }

    public StringProperty getCustomerNameProperty(){
        return customerNameProperty;
    }

    public void setParent(MainSceneController parent){
        parentController = parent;
    }

    @FXML private ScrollPane information;
    @FXML private InformationController informationController;
//    @FXML private Tab informationTab;

    @FXML private ScrollPane scramble;
    @FXML private ScrambleController scrambleController;
//    @FXML private Tab scrambleTab;

    @FXML private ScrollPane payment;
    @FXML private PaymentController paymentController;
//    @FXML private Tab paymentTab;

    @FXML private TabPane customerTabPane;

    public void initializeTabs() {
        //init information
        informationController.setParentController(this);
        informationController.getCustomerNameProperty().bind(customerNameProperty);

        //init scramble
        scrambleController.setParentController(this);

        //init payment
        paymentController.setParentController(this);
        paymentController.getCustomerNameProperty().bind(customerNameProperty);

        if (areControllersInitialized(informationController, scrambleController, paymentController)){
            customerTabPane.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
                switch (newValue.getText()){
                    case "Information":
                        informationController.onShow();
                        break;

                    case "Scramble":
                        scrambleController.onShow();
                        break;

                    case "Payment":
                        paymentController.onShow();
                }
            }));
        }
    }

    public TabPane getCustomerTabPane() {
        return customerTabPane;
    }

    public void chooseTab(int tabIndex){
        customerTabPane.getSelectionModel().select(1);
        customerTabPane.getSelectionModel().select(0);
    }

    @Override
    public MainSystem getModel() {
        return parentController.getModel();
    }
    @Override
    public void createExceptionDialog(Exception ex) {
        parentController.createExceptionDialog(ex);
    }
}
