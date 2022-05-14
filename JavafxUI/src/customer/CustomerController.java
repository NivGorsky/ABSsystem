package customer;
import DTO.CustomerDTO;
import Engine.ABSsystem;
import Engine.Customer;
import Engine.MainSystem;
import customer.information.InformationController;
import customer.payment.PaymentController;
import customer.scramble.ScrambleController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;


public class CustomerController {

    private StringProperty customerNameProperty;
    private CustomerDTO customerDTO;
    private MainSystem model;

    private boolean areControllersInitialized(InformationController customerController, ScrambleController scrambleController, PaymentController paymentController){

        return customerController != null && scrambleController != null && paymentController != null;
    }

    public void setModel(MainSystem model){
        this.model = model;
        this.informationController.setModel(model);
    }

    public void updateCustomerDTO(){
        customerDTO = model.getCustomerDTO(customerNameProperty.getValue());
    }

    public StringProperty getCustomerNameProperty(){
        return customerNameProperty;
    }

    public CustomerDTO getCustomerDTO(){
        return customerDTO;
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



}
