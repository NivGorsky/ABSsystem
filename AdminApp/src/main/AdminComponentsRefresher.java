package main;

import java.util.TimerTask;
import adminScene.AdminSceneController;

public class AdminComponentsRefresher extends TimerTask {

    private final AdminSceneController adminSceneToUpdate;
    
    public AdminComponentsRefresher(AdminSceneController adminSceneController) {
        this.adminSceneToUpdate = adminSceneController;
    }


    @Override
    public void run() {
        adminSceneToUpdate.onShow();
    }
}
