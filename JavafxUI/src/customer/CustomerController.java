package customer;
import Engine.ABSsystem;
import Engine.MainSystem;
import customer.information.InformationController;
import customer.payment.PaymentController;
import customer.scramble.ScrambleController;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;


public class CustomerController {

    private StringProperty customerNameProperty;
    private MainSystem model;

    public StringProperty getCustomerNameProperty(){
        return customerNameProperty;
    }

    @FXML private ScrollPane information;
    @FXML private InformationController informationController;
    @FXML private ScrollPane scramble;
    @FXML private ScrambleController scrambleController;
    @FXML private ScrollPane payment;
    @FXML private PaymentController paymentController;

    @FXML
    public void initialize(){

        if (areControllersInitialized(informationController, scrambleController, paymentController)){
            informationController.setParentController(this);
            scrambleController.setParentController(this);
            paymentController.setParentController(this);
        }









    }

    private boolean areControllersInitialized(InformationController customerController, ScrambleController scrambleController, PaymentController paymentController){

        return customerController != null && scrambleController != null && paymentController != null;
    }
    public void setModel(MainSystem model){
        this.model = model;
        this.informationController.setModel(model);
    }



}
