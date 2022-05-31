package adminScene;
import Engine.Customer;
import Engine.MainSystem;
import Exceptions.XMLFileException;
import customersInfoTable.CustomersInfoTableController;
import exceptionDialog.ExceptionDialogCreator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ScrollPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import loansTable.LoansTableComponentController;
import mainScene.MainSceneController;
import mutualInterfaces.ParentController;

import javax.xml.bind.JAXBException;
import java.io.File;

public class AdminSceneController implements ParentController {

    @FXML private Button increaseYazButton;
    @FXML private Button loadFileButton;

    @FXML private ScrollPane loansTableComponent;
    @FXML private LoansTableComponentController loansTableComponentController;

    @FXML private TableView<Customer> customersInfoTableView;
    @FXML private CustomersInfoTableController customersInfoTableController;

    private MainSceneController parentController;

    @FXML public void initialize()
    {
        if(loansTableComponentController != null)
        {
            loansTableComponentController.setParentController(this);
            customersInfoTableController.setParentController(this);
        }
    }

    public void setIncreaseYAZButtonDisable(SimpleBooleanProperty isFileSelected)
    {
        increaseYazButton.disableProperty().bind(isFileSelected.not());
    }

    @FXML public void increaseYazButtonClicked()
    {
        parentController.getModel().moveTimeLine();
        int yaz = parentController.getModel().getCurrYaz();
        parentController.getHeaderController().setCurrentYAZProperty(yaz);
        this.onShow();
    }

    @FXML public void loadFileButtonClicked()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        File selectedFile = fileChooser.showOpenDialog(parentController.getPrimaryStage());

        try {
            parentController.getModel().loadXML(selectedFile.getPath());
            parentController.setFileInfo(selectedFile.getPath());
            loansTableComponentController.loadLoansData();
            customersInfoTableController.loadCustomersInfo();
        }
        catch (XMLFileException | JAXBException ex) {
            ExceptionDialogCreator.createExceptionDialog(ex);
        }
    }

    public void setParentController(MainSceneController parentController)
    {
        this.parentController = parentController;
    }
    public MainSystem getModel() { return parentController.getModel(); }

    @Override
    public void createExceptionDialog(Exception ex) {
        parentController.createExceptionDialog(ex);
    }

    public void onShow(){
        //need to add methods for on show for the customer information

        //reloading the loans data
        loansTableComponentController.clearTable();
        loansTableComponentController.loadLoansData();
    }
}
