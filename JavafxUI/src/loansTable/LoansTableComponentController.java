package loansTable;

import DTO.LoanDTO;
import Engine.MainSystem;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import jsonDeserializer.LoanDtoDeserializer;
import main.Configurations;
import mutualInterfaces.ParentController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import loansTable.loansAdditionalInfo.ActiveInfoController;
import loansTable.loansAdditionalInfo.FinishedInfoController;
import loansTable.loansAdditionalInfo.InRiskInfoController;
import loansTable.loansAdditionalInfo.PendingInfoController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import com.google.gson.reflect.TypeToken;
import java.io.Console;
import java.lang.reflect.Type;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class LoansTableComponentController implements ParentController {

    @FXML private TableView<LoanDTO> loansTable;
    @FXML private TableColumn<LoanDTO, String> loanNameCol;
    @FXML private TableColumn<LoanDTO, String> loanerNameCol;
    @FXML private TableColumn<LoanDTO, String> categoryCol;
    @FXML private TableColumn<LoanDTO, Integer> initialAmountCol;
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
    private LoanDTO selectedLoanFromTable;


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

        changeTableSelectionModelToSingle();
        setLoanSelectionListener((observable, oldValue, newValue) -> {
            setSelectedLoanFromLoansTable(newValue);
        });
    }

    private void setSelectedLoanFromLoansTable(LoanDTO selectedLoan) {
        this.selectedLoanFromTable = selectedLoan;
    }

    public LoanDTO getSelectedLoanFromTable() { return selectedLoanFromTable; }

    @Override
    public MainSystem getModel() {
        return parentController.getModel();
    }

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

    @Override
    public String getLoggedInUser() {
       return parentController.getLoggedInUser();
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
                    FXMLLoader loader = loadFile("loansAdditionalInfo/pendingInfo.fxml");
                    this.pendingInfoController = loader.getController();
                    pendingInfoController.setData(loan);
                    break;
                }
                case "ACTIVE": {
                    FXMLLoader loader = loadFile("loansAdditionalInfo/activeInfo.fxml");
                    this.activeInfoController = loader.getController();
                    activeInfoController.setData(loan);
                    break;
                }
                case "IN_RISK": {
                    FXMLLoader loader = loadFile("loansAdditionalInfo/inRiskInfo.fxml");
                    this.inRiskInfoController = loader.getController();
                    inRiskInfoController.setData(loan);
                    break;
                }
                case "FINISHED": {
                    FXMLLoader loader =loadFile("loansAdditionalInfo/finishedInfo.fxml");
                    this.finishedInfoController = loader.getController();
                    finishedInfoController.setData(loan);
                    break;
                }
            }
        }
    }

    public void loadLoansData() {
        createTableLoanColumns();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/showLoansInfo").newBuilder();
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder().url(finalUrl).get().build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);

        Callback showLoansInfoCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    parentController.createExceptionDialog(e);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String rawBody = response.body().string();
                    Type arrayListLoanDtoType = new TypeToken<ArrayList<LoanDTO>>(){}.getType();
                    ArrayList<LoanDTO> loans = Configurations.GSON.fromJson(rawBody, arrayListLoanDtoType);

                    Platform.runLater(() -> {
                        putLoansInTable(loans);
                    });
                }

                else{
                    parentController.createExceptionDialog(new Exception(Integer.toString(response.code())));
                }

                response.close();
            }
        };

        call.enqueue(showLoansInfoCallBack);
    }

    public void loadSpecificCustomerLoansAsLender(String customerName){
        createTableLoanColumns();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/showLoansInfo").newBuilder();
        urlBuilder.addQueryParameter("customer-name", customerName);
        urlBuilder.addQueryParameter("loan-type", "lender");

        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder().url(finalUrl).get().build();
        Call call = Configurations.HTTP_CLIENT.newCall(request);

        Callback showLoansInfoCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    parentController.createExceptionDialog(e);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String rawBody = response.body().string();
                    Type arrayListLoanDtoType = new TypeToken<ArrayList<LoanDTO>>(){}.getType();
                    ArrayList<LoanDTO> loans = Configurations.GSON.fromJson(rawBody, arrayListLoanDtoType);

                    Platform.runLater(() -> {
                        putLoansInTable(loans);
                    });
                }

                else{
                    parentController.createExceptionDialog(new Exception(Integer.toString(response.code())));
                }

                response.close();
            }
        };

        call.enqueue(showLoansInfoCallBack);

    }

    public void loadSpecificCustomerLoansAsBorrower(String customerName){
        createTableLoanColumns();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/showLoansInfo").newBuilder();
        urlBuilder.addQueryParameter("customer-name", customerName);
        urlBuilder.addQueryParameter("loan-type", "borrower");

        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder().url(finalUrl).get().build();
        Call call = Configurations.HTTP_CLIENT.newCall(request);

        Callback showLoansInfoCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    parentController.createExceptionDialog(e);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String rawBody = response.body().string();
                    Type arrayListLoanDtoType = new TypeToken<ArrayList<LoanDTO>>(){}.getType();

                    Platform.runLater(() -> {
                        ArrayList<LoanDTO> loans = Configurations.GSON.fromJson(rawBody, arrayListLoanDtoType);
                        putLoansInTable(loans);
                    });
                }

                else{
                    parentController.createExceptionDialog(new Exception(Integer.toString(response.code())));
                }

                response.close();
            }
        };

        call.enqueue(showLoansInfoCallBack);
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
    }

    public void clearTable(){
        loansTable.getItems().clear();
    }

    private FXMLLoader loadFile(String path)
    {
        FXMLLoader loader = new FXMLLoader();
        try {
            URL url = getClass().getResource(path);
            loader.setLocation(url);
            ScrollPane container = loader.load(url.openStream());
            additionalInfoScrollPane.setContent(container);
        }
        catch (Exception ex) {
            parentController.createExceptionDialog(ex);
        }

        return loader;
    }
    public void changeTableSelectionModelToSingle(){
        loansTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void setLoanSelectionListener(ChangeListener<LoanDTO> changeListener){
        loansTable.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }
}


