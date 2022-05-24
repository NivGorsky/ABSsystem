package loansTable;

import DTO.LoanDTO;
import Engine.MainSystem;
import adminScene.AdminSceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mutualInterfaces.ParentController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import loansTable.loansAdditionalInfo.ActiveInfoController;
import loansTable.loansAdditionalInfo.FinishedInfoController;
import loansTable.loansAdditionalInfo.InRiskInfoController;
import loansTable.loansAdditionalInfo.PendingInfoController;

import java.net.URL;
import java.util.ArrayList;

public class LoansTableComponentController implements ParentController {

    @FXML private TableView<LoanDTO> loansTable;
    @FXML private TableColumn<LoanDTO, String> loanNameCol;
    @FXML private TableColumn<LoanDTO, String> loanerNameCol;
    @FXML private TableColumn<LoanDTO, String> categoryCol;
    @FXML private TableColumn<LoanDTO, Double> initialAmountCol;
    @FXML private TableColumn<LoanDTO, Integer> totalYazCol;
    @FXML private TableColumn<LoanDTO, Double> totalInterestCol;
    @FXML private TableColumn<LoanDTO, Integer> PaymentRateCol;
    @FXML private TableColumn<LoanDTO, String> statusCol;
    @FXML private ScrollPane additionalInfoScrollPane;
    @FXML private Label noContentLabel;
    @FXML private PendingInfoController pendingInfoController;
    @FXML private ActiveInfoController activeInfoController;
    @FXML private InRiskInfoController inRiskInfoController;
    @FXML private FinishedInfoController finishedInfoController;

    private ArrayList<LoanDTO> loans;
    private ParentController parentController;


    @FXML public void initialize()
    {
        if(pendingInfoController != null && activeInfoController!= null &&
                inRiskInfoController != null && finishedInfoController != null)
        {
            pendingInfoController.setParentController(this);
            activeInfoController.setParentController(this);
            inRiskInfoController.setParentController(this);
            finishedInfoController.setParentController(this);
        }

        loansTable.setRowFactory( tv -> {
            TableRow<LoanDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    LoanDTO rowData = row.getItem();
                    noContentLabel.visibleProperty().setValue(false);
                    displayAdditionalDetails(rowData.getLoanName());
                }
            });
            return row ;
        });
    }

    @Override
    public MainSystem getModel() {
        return parentController.getModel();
    }

    public void setParentController(ParentController adminSceneController) {
        parentController = adminSceneController;
    }

    private void displayAdditionalDetails(String loanName)
    {
        LoanDTO loan = null;

        for(LoanDTO l : loans)
        {
            if(l.getLoanName().equals(loanName))
            {
                loan=l;
                break;
            }
        }

        if(loan != null)
        {
            switch (loan.getStatus())
            {
                case "PENDING": {
                    FXMLLoader loader = loadFile("pendingInfo.fxml");
                    this.pendingInfoController = loader.getController();
                    pendingInfoController.setData(loan);
                }
                case "ACTIVE": {
                    FXMLLoader loader = loadFile("activeInfo.fxml");
                    this.activeInfoController = loader.getController();
                    activeInfoController.setData(loan);
                }
                case "IN_RISK": {
                    FXMLLoader loader = loadFile("inRiskInfo.fxml");
                    this.inRiskInfoController = loader.getController();
                    inRiskInfoController.setData(loan);
                }
                case "FINISHED": {
                    FXMLLoader loader =loadFile("finishedInfo.fxml");
                    this.finishedInfoController = loader.getController();
                    finishedInfoController.setData(loan);
                }
            }
        }
    }

    public void loadLoansData()
    {
        createTableLoanColumns();
        ArrayList<LoanDTO> loans = parentController.getModel().showLoansInfo();
        putLoansInTable(loans);
    }

    public void loadSpecificCustomerLoansAsLender(String customerName){
        createTableLoanColumns();
        ArrayList<LoanDTO> loans = parentController.getModel().getLoansByCustomerNameAsLender(customerName);
        putLoansInTable(loans);
    }

    public void loadSpecificCustomerLoansAsBorrower(String customerName){
        createTableLoanColumns();
        ArrayList<LoanDTO> loans = parentController.getModel().getLoansByCustomerNameAsBorrower(customerName);
        putLoansInTable(loans);
    }

    private void putLoansInTable( ArrayList<LoanDTO> loans){
        ObservableList<LoanDTO> loansForTable = FXCollections.observableArrayList();
        loansForTable.addAll(loans);
        loansTable.setItems(loansForTable);
    }

    public void createTableLoanColumns(){
        loanNameCol.setCellValueFactory(cellData -> cellData.getValue().getLoanNameProperty());
        loanerNameCol.setCellValueFactory(cellData -> cellData.getValue().getCustomerNameProperty());
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());
        initialAmountCol.setCellValueFactory(cellData -> cellData.getValue().getInitialAmountProperty().asObject());
        totalYazCol.setCellValueFactory(cellData -> cellData.getValue().getMaxYazToPayProperty().asObject());
        totalInterestCol.setCellValueFactory(cellData -> cellData.getValue().getTotalInterestProperty().asObject());
        PaymentRateCol.setCellValueFactory(cellData -> cellData.getValue().getYazPerPaymentProperty().asObject());
        statusCol.setCellValueFactory(cellData -> cellData.getValue().getStatusProperty());

        this.loans = parentController.getModel().showLoansInfo();
        ObservableList<LoanDTO> loansForTable = FXCollections.observableArrayList();
        loansForTable.addAll(loans);

        loansTable.setItems(loansForTable);
    }
    public void clearTable(){
        loansTable.getItems().clear();
    }

    private FXMLLoader loadFile(String path)
    {
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader();
            URL url = getClass().getResource("inRiskInfo.fxml");
            loader.setLocation(url);
            ScrollPane container = loader.load(url.openStream());
            additionalInfoScrollPane.setContent(container);
        }
        catch (Exception ex)
        {

        }

        return null;
    }
}


