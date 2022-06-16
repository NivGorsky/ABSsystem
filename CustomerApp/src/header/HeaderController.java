package header;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javax.xml.bind.JAXBException;
import java.io.File;

public class HeaderController {

    @FXML private GridPane header;
    @FXML private Button loadFileButton;
    @FXML private Label currentYazLabel;
    @FXML private ComboBox<String> displayModeCB;
    @FXML private Label heyCustomerLabel;

    private ParentController parentController;

    private SimpleBooleanProperty isFileSelected;
    private SimpleIntegerProperty currentYAZ;

    ObservableList<String> displayModeOptions =  FXCollections.observableArrayList("Light Mode", "Dark Mode", "MTA Mode", "Barbi Mode");

    public HeaderController()
    {
        isFileSelected = new SimpleBooleanProperty(false);
        currentYAZ = new SimpleIntegerProperty(1);
    }

    @FXML public void initialize() {
        displayModeCB.setItems(displayModeOptions);
        currentYazLabel.textProperty().bind(Bindings.concat("Current YAZ: ", currentYAZ));
        currentYazLabel.disableProperty().bind(isFileSelected.not());
        displayModeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            parentController.switchStyleSheet(displayModeCB.getSelectionModel().getSelectedItem());
        });
    }

    @FXML
    void loadFileButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        File selectedFile = fileChooser.showOpenDialog(parentController.getPrimaryStage());

        try {
            parentController.getModel().loadXML(selectedFile.getPath());
            parentController.setFileInfo(selectedFile.getPath());
        }
        catch (XMLFileException | JAXBException ex) {
            ExceptionDialogCreator.createExceptionDialog(ex);
        }
    }

    public SimpleBooleanProperty getIsFileSelectedProperty() {
        return isFileSelected;
    }
    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }}
    public void setCurrentYAZProperty(int newCurrentYaz) { currentYAZ.set(newCurrentYaz); }
    public void setIsFileSelectedProperty(Boolean isSelected) { isFileSelected.set(isSelected);}
}

//TODO: bind customer name after login to the label in the header