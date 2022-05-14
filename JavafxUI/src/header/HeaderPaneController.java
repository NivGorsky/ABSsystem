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
import mainScene.MainSceneController;

public class HeaderPaneController {

    @FXML private ComboBox<String> viewByCB;
    @FXML private Label filePathLabel;
    @FXML private Label currentYazLabel;

    private MainSceneController parentController;

    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFilePath;
    private SimpleIntegerProperty currentYAZ;

    ObservableList<String> options =  FXCollections.observableArrayList("Admin" , "Customer");

    public HeaderPaneController()
    {
        isFileSelected = new SimpleBooleanProperty();
        selectedFilePath = new SimpleStringProperty();
        currentYAZ = new SimpleIntegerProperty();
    }

    @FXML public void initialize()
    {
       viewByCB.setItems(options);
       viewByCB.getSelectionModel().selectFirst(); //always start as admin and disable option to customer until file loaded
       filePathLabel.textProperty().bind(Bindings.format("File Path: ", selectedFilePath));
       currentYazLabel.textProperty().bind(Bindings.format("Current YAZ: ", currentYAZ));
       viewByCB.disableProperty().bind(isFileSelected.not()); //TODO: check if i did it ok
    }

    public void setParentController(MainSceneController parentController)
    {
        this.parentController = parentController;
    }

    public void setSelectedFilePathProperty(String path) { selectedFilePath.set(path); }
    public void setCurrentYAZProperty(int newCurrentYaz) { currentYAZ.set(newCurrentYaz); }
    public void setIsFileSelectedProperty(Boolean isSelected) { isFileSelected.set(isSelected);}

}
