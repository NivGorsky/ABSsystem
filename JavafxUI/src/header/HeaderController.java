package header;

import Engine.MainSystem;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import mainScene.MainSceneController;

import java.util.ArrayList;

public class HeaderController {

    @FXML private ComboBox<String> viewByCB;
    @FXML private ComboBox<String> displayModeCB;
    @FXML private Label filePathLabel;
    @FXML private Label currentYazLabel;

    private MainSceneController parentController;

    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFilePath;
    private SimpleIntegerProperty currentYAZ;

    ObservableList<String> viewByOptions =  FXCollections.observableArrayList("Admin");
    ObservableList<String> displayModeOptions =  FXCollections.observableArrayList("Light Mode", "Dark Mode", "MTA Mode", "Barbi Mode");

    public HeaderController()
    {
        isFileSelected = new SimpleBooleanProperty(false);
        selectedFilePath = new SimpleStringProperty("File not loaded");
        currentYAZ = new SimpleIntegerProperty(1);
    }

    public ReadOnlyObjectProperty<String> getChosenCustomerNameProperty(){
        return viewByCB.getSelectionModel().selectedItemProperty();
    }

    @FXML public void initialize()
    {
       displayModeCB.setItems(displayModeOptions);
       viewByCB.setItems(viewByOptions);
       viewByCB.getSelectionModel().selectFirst(); //always start as admin and disable option to customer until file loaded
       filePathLabel.textProperty().bind(Bindings.concat("File Path: ", selectedFilePath));
       currentYazLabel.textProperty().bind(Bindings.concat("Current YAZ: ", currentYAZ));
       viewByCB.disableProperty().bind(isFileSelected.not());
       viewByCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           parentController.switchBody(viewByCB.getSelectionModel().getSelectedItem());
       });
       currentYazLabel.disableProperty().bind(isFileSelected.not());
       isFileSelected.addListener(((observable, oldValue, newValue) -> {
           ArrayList<String> customerNames = parentController.getCustomers();
           viewByOptions.addAll(FXCollections.observableArrayList(customerNames));
           viewByCB.setItems(viewByOptions);;}));

       displayModeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           parentController.switchStyleSheet(displayModeCB.getSelectionModel().getSelectedItem());
       });
    }

    public void setParentController(MainSceneController parentController)
    {
        this.parentController = parentController;
    }
    public void setSelectedFilePathProperty(String path) { selectedFilePath.set(path); }
    public void setCurrentYAZProperty(int newCurrentYaz) { currentYAZ.set(newCurrentYaz); }
    public void setIsFileSelectedProperty(Boolean isSelected) { isFileSelected.set(isSelected);}


}
