package adminScene;

import Engine.Customer;
import Engine.MainSystem;
import customersInfoTable.CustomersInfoTableController;
import javafx.scene.control.ScrollPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import loansTable.LoansTableComponentController;
import mainScene.MainSceneController;

import java.io.File;

public class AdminSceneController {

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
        }
    }


    @FXML public void increaseYazButtonClicked()
    {}

    @FXML public void loadFileButtonClicked()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        File selectedFile = fileChooser.showOpenDialog(parentController.getPrimaryStage());

        if(selectedFile == null) {
           //exception;
        }

        try {
            parentController.getModel().loadXML(selectedFile.getPath());
        }
        catch (Exception ex) {
            //System.out.println(ex.getMessage());
            //show error
        }

        parentController.setFileInfo(selectedFile.getPath());

        //loansTableController.loadLoansData();
      //  loadCustomersInfo();


    }

    public void setParentController(MainSceneController parentController)
    {
        this.parentController = parentController;
    }
    public MainSystem getModel() { return parentController.getModel(); }
}
