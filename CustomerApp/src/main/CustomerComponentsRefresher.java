package main;
import customerScene.CustomerSceneController;

import java.util.TimerTask;

public class CustomerComponentsRefresher extends TimerTask {

    CustomerSceneController customerSceneToUpdate;

    public CustomerComponentsRefresher(CustomerSceneController customerSceneController) {
        this.customerSceneToUpdate = customerSceneController;
    }

    @Override
    public void run() {
        customerSceneToUpdate.onShow();
    }

}
