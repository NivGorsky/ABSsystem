package mutualInterfaces;

import Engine.MainSystem;
import javafx.stage.Stage;

public interface ParentController {
    MainSystem getModel();
    void createExceptionDialog(Exception ex);
    Stage getPrimaryStage();
    void switchStyleSheet(String selectedItem);
}
