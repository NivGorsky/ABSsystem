package customer;
import customer.information.InformationController;
import customer.payment.PaymentController;
import customer.scramble.ScrambleController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;


public class CustomerController {
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




}
