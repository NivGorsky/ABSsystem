package mutualInterfaces;

import javafx.stage.Stage;

public interface ParentController {
    void createExceptionDialog(Exception ex);
    Stage getPrimaryStage();
    void switchStyleSheet(String selectedItem);
    String getLoggedInUser();

}
