package header;

import DTO.CustomerDTO;
import Engine.MainSystem;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import mainScene.MainSceneController;

import java.util.ArrayList;

public class HeaderController {

    @FXML private ComboBox<String> viewByCB;
    @FXML private Label filePathLabel;
    @FXML private Label currentYazLabel;

    private MainSceneController parentController;

    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFilePath;
    private SimpleIntegerProperty currentYAZ;
    private MainSystem model;

    ObservableList<String> options =  FXCollections.observableArrayList("Admin");

    public HeaderController()
    {
        isFileSelected = new SimpleBooleanProperty(false);
        selectedFilePath = new SimpleStringProperty("File not loaded");
        currentYAZ = new SimpleIntegerProperty(1);
    }

    @FXML public void initialize()
    {
       viewByCB.setItems(options);
       viewByCB.getSelectionModel().selectFirst(); //always start as admin and disable option to customer until file loaded
       filePathLabel.textProperty().bind(Bindings.concat("File Path: ", selectedFilePath));
       currentYazLabel.textProperty().bind(Bindings.concat("Current YAZ: ", currentYAZ));
       viewByCB.disableProperty().bind(isFileSelected.not());
       currentYazLabel.disableProperty().bind(isFileSelected.not());
       isFileSelected.addListener(((observable, oldValue, newValue) -> {
           ArrayList<String> customerNames = model.getCustomersNames();
           options.addAll(FXCollections.observableArrayList(customerNames));
           viewByCB.setItems(options);;}));
    }

    public void setParentController(MainSceneController parentController)
    {
        this.parentController = parentController;
    }
    public void setSelectedFilePathProperty(String path) { selectedFilePath.set(path); }
    public void setCurrentYAZProperty(int newCurrentYaz) { currentYAZ.set(newCurrentYaz); }
    public void setIsFileSelectedProperty(Boolean isSelected) { isFileSelected.set(isSelected);}
    public void setModel(MainSystem model){this.model = model;}
    public void getCustomersNamesFromModelToComboBox(){
        ArrayList<String> customerNames = model.getCustomersNames();
        options.addAll(FXCollections.observableArrayList(customerNames));
        viewByCB.setItems(options);
    }
}
