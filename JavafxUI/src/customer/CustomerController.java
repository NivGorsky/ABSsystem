package customer;
import customer.information.InformationController;
import customer.payment.PaymentController;
import customer.scramble.ScrambleController;
import javafx.fxml.FXML;

import java.awt.*;

public class CustomerController {
    @FXML private ScrollPane informationComponent;
    @FXML private InformationController informationComponentController;

    @FXML private ScrollPane scrambleComponent;
    @FXML private ScrambleController scrambleComponentController;

    @FXML private ScrollPane paymentComponent;
    @FXML private PaymentController paymentComponentController;

    @FXML
    public void initialize(){

        if (areControllersInitialized(informationComponentController, scrambleComponentController, paymentComponentController)){
            informationComponentController.setParentController(this);
            scrambleComponentController.setParentController(this);
            paymentComponentController.setParentController(this);
        }
    }

    private boolean areControllersInitialized(InformationController customerController, ScrambleController scrambleController, PaymentController paymentController){

        return customerController != null && scrambleController != null && paymentController != null;
    }




}
