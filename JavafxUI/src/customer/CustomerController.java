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
import mainScene.MainSceneController;


public class CustomerController {

    private MainSystem model;
    private MainSceneController parentController;

    private boolean areControllersInitialized(InformationController customerController, ScrambleController scrambleController, PaymentController paymentController){

        return customerController != null && scrambleController != null && paymentController != null;
    }

    public void setModel(MainSystem model){
        this.model = model;
        this.informationController.setModel(model);
    }

    public void setParent(MainSceneController parent){
        parentController = parent;
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
