package adminScene;
import DTO.CustomerDTO;
import Engine.MainSystem;
import Exceptions.XMLFileException;
import customersInfoTable.CustomersInfoTableController;
import exceptionDialog.ExceptionDialogCreator;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import loansTable.LoansTableComponentController;
import mainScene.MainSceneController;
import mutualInterfaces.ParentController;

import javax.xml.bind.JAXBException;
import java.io.File;

public class AdminSceneController implements ParentController {

    @FXML private GridPane header;
    @FXML private Label currentYazLabel;
    @FXML private ComboBox<String> displayModeCB;
    @FXML private Label heyAdminLabel;

    @FXML private Button increaseYazButton;
    @FXML private Button decreaseYazButton;
    @FXML private Button loadFileButton;

    @FXML private Label loansLabel;
    @FXML private ScrollPane loansTableComponent;
    @FXML private LoansTableComponentController loansTableComponentController;

    @FXML private Label customersInfoLabel;
    @FXML private TableView<CustomerDTO> customersInfoTableView;
    @FXML private CustomersInfoTableController customersInfoTableController;

    private SimpleIntegerProperty currentYAZ = new SimpleIntegerProperty(1);
    private ParentController parentController;

    ObservableList<String> displayModeOptions =  FXCollections.observableArrayList("Light Mode", "Dark Mode", "MTA Mode", "Barbi Mode");

    @FXML public void initialize()
    {
        displayModeCB.setItems(displayModeOptions);
        currentYazLabel.textProperty().bind(Bindings.concat("Current YAZ: ", currentYAZ));
        displayModeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            parentController.switchStyleSheet(displayModeCB.getSelectionModel().getSelectedItem());
        });

        if(loansTableComponentController != null && customersInfoTableController != null)
        {
            loansTableComponentController.setParentController(this);
            customersInfoTableController.setParentController(this);

            loansTableComponentController.loadLoansData();
            customersInfoTableController.loadCustomersInfo();
        }

    }

    public void setIncreaseYAZButtonDisable(SimpleBooleanProperty isFileSelected)
    {
        increaseYazButton.disableProperty().bind(isFileSelected.not());
    }

    @FXML public void increaseYazButtonClicked() {
        parentController.getModel().moveTimeLine();
        int yaz = parentController.getModel().getCurrYaz();
        currentYAZ.set(yaz);
        this.onShow();
    }

    @FXML public void decreaseYAZButtonClicked() {
        //TODO
    }

    public void setParentController(ParentController parentController)
    {
        this.parentController = parentController;
    }
    public MainSystem getModel() { return parentController.getModel(); }

    @Override
    public void createExceptionDialog(Exception ex) {
        parentController.createExceptionDialog(ex);
    }

    @Override
    public Stage getPrimaryStage() {
        return parentController.getPrimaryStage();
    }

    @Override
    public void switchStyleSheet(String selectedItem) {
        parentController.switchStyleSheet(selectedItem);
    }

    public void onShow(){
        loansTableComponentController.clearTable();
        loansTableComponentController.loadLoansData();
    }
}


