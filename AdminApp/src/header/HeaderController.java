package header;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class HeaderController {

    @FXML private GridPane header;
    @FXML private Label currentYazLabel;
    @FXML private ComboBox<String> displayModeCB;
    @FXML private Label heyAdminLabel;

    private SimpleIntegerProperty currentYAZ;
    private MainSceneController parentController;


    ObservableList<String> displayModeOptions =  FXCollections.observableArrayList("Light Mode", "Dark Mode", "MTA Mode", "Barbi Mode");

    public HeaderController() {
        currentYAZ = new SimpleIntegerProperty(1);
    }

    @FXML public void initialize() {
        displayModeCB.setItems(displayModeOptions);
        currentYazLabel.textProperty().bind(Bindings.concat("Current YAZ: ", currentYAZ));
        displayModeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            parentController.switchStyleSheet(displayModeCB.getSelectionModel().getSelectedItem());
        });
    }

    public void setParentController(MainSceneController parentController) {
        this.parentController = parentController;
    }
    public void setCurrentYAZProperty(int newCurrentYaz) { currentYAZ.set(newCurrentYaz); }
}
