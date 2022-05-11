package customer.payment;

import customer.CustomerController;

public class PaymentController {

    private CustomerController parentController;

    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }

}
