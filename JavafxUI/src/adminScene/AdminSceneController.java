package adminScene;

import Engine.Customer;
import Engine.Loan;
import Engine.MainSystem;
import customersInfoTable.CustomersInfoTableController;
import loansTable.LoansTableController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import mainScene.MainSceneController;

import java.io.File;

public class AdminSceneController {

    @FXML private Button increaseYazButton;
    @FXML private Button loadFileButton;
    @FXML private TableView<Loan> loansTableView;
    @FXML private LoansTableController loansTableController;
    @FXML private TableView<Customer> customersInfoTableView;
    @FXML private CustomersInfoTableController customersInfoTableController;

    private MainSceneController parentController;


    @FXML public void increaseYazButtonClicked()
    {

    }

    @FXML public void loadFileButtonClicked()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");

        File selectedFile = fileChooser.showOpenDialog(parentController.getPrimaryStage());
        if(selectedFile == null) {
            return; //TODO: exception?
        }

        String path = selectedFile.getAbsolutePath();
        parentController.getHeaderPaneController().setSelectedFilePathProperty(path);
        parentController.getHeaderPaneController().setIsFileSelectedProperty(true);


    }

    public void setParentController(MainSceneController parentController)
    {
        this.parentController = parentController;
    }
    public MainSystem getModel() { return parentController.getModel(); }
}
