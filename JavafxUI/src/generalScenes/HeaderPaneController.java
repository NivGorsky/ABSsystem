package generalScenes;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class HeaderPaneController {

    @FXML private ComboBox<?> viewByCB;
    @FXML private Label filePathLabel;
    @FXML private Label currentYazLabel;


    public Label getFilePathLabel() { return filePathLabel; }
    public Label getCurrentYazLabel() { return currentYazLabel; }

}
