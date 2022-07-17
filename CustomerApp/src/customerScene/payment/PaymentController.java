package customerScene.payment;

import DTO.AccountMovementDTO;
import DTO.LoanDTO;
import DTO.NotificationsDTO;
import DTO.UIPaymentDTO;
import Engine.MainSystem;
import com.google.gson.reflect.TypeToken;
import customerScene.CustomerSceneController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import loansTable.LoansTableComponentController;
import main.Configurations;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class PaymentController implements ParentController {

    private CustomerSceneController parentController;
    private StringProperty customerNameProperty;
    private final ObservableList<NotificationsDTO.NotificationDTO> notifications;
    private LoanDTO selectedLoanFromLoansTable;

    @FXML ScrollPane borrowerLoansTableComponent;
    @FXML LoansTableComponentController borrowerLoansTableComponentController;
    @FXML private Accordion notificationsBoard;
    @FXML private TableColumn<LoanDTO.LenderDetailsDTO, String> lendersNameTableColumn;
    @FXML private TableColumn<LoanDTO.LenderDetailsDTO, Double> lendersAmountTableColumn;
    @FXML private TableView<LoanDTO.LenderDetailsDTO> lendersTableView;
    @FXML private Button payToLenderButton;
    @FXML private Button payToAllLendersButton;
    @FXML private Button closeLoanButton;
    @FXML private TextField payDebtTextField;
    @FXML private Button payDebtButton;
    @FXML private Label debtLabel;

    public void initialize(){
        borrowerLoansTableComponentController.setParentController(this);
        initNotifications();
        borrowerLoansTableComponentController.changeTableSelectionModelToSingle();
        borrowerLoansTableComponentController.setLoanSelectionListener((observable, oldValue, newValue) -> {
            LoanWasSelectedFromLoansTable(newValue);
            setSelectedLoanFromLoansTable(newValue); //TODO
        });
        initLendersTable();
        initPaymentButtons();
        payDebtButton.setDisable(true);
        payDebtTextField.setDisable(true);
        debtLabel.setText("Debt: ");
    }

    public PaymentController(){
        notifications = FXCollections.observableArrayList();
        customerNameProperty = new SimpleStringProperty();
        selectedLoanFromLoansTable = null;
    }

    private void postPayment(UIPaymentDTO uiPaymentDTO){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/payment").newBuilder();
        String finalUrl = urlBuilder.build().toString();
        String uiPaymentDtoAsJson = Configurations.GSON.toJson(uiPaymentDTO);

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(uiPaymentDtoAsJson.getBytes()))
                .build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback payDebtCallBack = new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody =  response.body().string();
                int responseCode = response.code();
                boolean isResponseSuccessful = response.isSuccessful();
                response.close();

                if (!isResponseSuccessful) {
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(Integer.toString(responseCode)
                                    + "\n" + responseBody)));
                }
            }
        };

        call.enqueue(payDebtCallBack);
    }

    @FXML
    void payDebtButtonClicked(ActionEvent event){
        UIPaymentDTO uiPaymentDTO = new UIPaymentDTO();
        uiPaymentDTO.loanDTO = selectedLoanFromLoansTable;
        uiPaymentDTO.amount = Double.parseDouble(payDebtTextField.getText());
        uiPaymentDTO.yaz = parentController.getCurrentYazProperty().getValue();
        uiPaymentDTO.customerName = parentController.getCustomerNameProperty().getValue();
        uiPaymentDTO.operation = "payDebt";

        postPayment(uiPaymentDTO);
    }

    @FXML
    void closeLoanButtonClicked(ActionEvent event) {
        UIPaymentDTO uiPaymentDTO = new UIPaymentDTO();
        uiPaymentDTO.loanDTO = selectedLoanFromLoansTable;
        uiPaymentDTO.amount =  selectedLoanFromLoansTable.getDebt();
        uiPaymentDTO.yaz = parentController.getCurrentYazProperty().getValue();
        uiPaymentDTO.customerName = this.customerNameProperty.getValue();
        uiPaymentDTO.operation = "closeLoan";

        postPayment(uiPaymentDTO);
    }

    @FXML
    void payToAllLendersButtonClicked(ActionEvent event) {
        UIPaymentDTO uiPaymentDTO = new UIPaymentDTO();
        uiPaymentDTO.loanDTO = selectedLoanFromLoansTable;
        uiPaymentDTO.yaz = parentController.getCurrentYazProperty().getValue();
        uiPaymentDTO.customerName = parentController.getCustomerNameProperty().getValue();
        uiPaymentDTO.operation = "payToAllLenders";

        postPayment(uiPaymentDTO);
    }

    @FXML
    void payToLenderButtonClicked(ActionEvent event) {
        UIPaymentDTO uiPaymentDTO = new UIPaymentDTO();
        uiPaymentDTO.loanDTO = selectedLoanFromLoansTable;
        uiPaymentDTO.yaz = parentController.getCurrentYazProperty().getValue();
        uiPaymentDTO.lenderDetailsDTO = lendersTableView.getSelectionModel().getSelectedItem();
        uiPaymentDTO.customerName = parentController.getCustomerNameProperty().getValue();
        uiPaymentDTO.operation = "payToLender";

        postPayment(uiPaymentDTO);
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

    public void setSelectedLoanFromLoansTable(LoanDTO selectedLoan){
        this.selectedLoanFromLoansTable = selectedLoan;
    }

    public StringProperty getCustomerNameProperty(){return this.customerNameProperty;}

    public void setParentController(CustomerSceneController parentController){
        this.parentController = parentController;
    }

    private void initNotifications(){
        notificationsBoard.getPanes().clear();
        wireNotificationsListeners();
    }

    private void initPaymentButtons(){
       lendersTableView.getItems().addListener((ListChangeListener<Object>) c -> {
            payToAllLendersButton.setDisable(lendersTableView.getItems().isEmpty());
        });

    }

    private void initLendersTable(){
        createTableViewColumns();
        lendersTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lendersTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           payToLenderButton.setDisable(lendersTableView.getSelectionModel().isEmpty());
        });
    }

    private void createTableViewColumns(){
        lendersNameTableColumn.setCellValueFactory(cellData -> cellData.getValue().getLenderNameProperty());
        lendersAmountTableColumn.setCellValueFactory(cellData -> cellData.getValue().getLendersInvestAmountProperty().asObject());
    }

    private void wireNotificationsListeners(){
        notifications.addListener((ListChangeListener<Object>) c -> {
            notificationsBoard.getPanes().clear();
            List<TitledPane> notificationsPanes = createNotificationsPanes();
            notificationsBoard.getPanes().addAll(notificationsPanes);
            notificationsBoard.setExpandedPane(notificationsBoard.getPanes().get(0));
        });
    }

    private List<TitledPane> createNotificationsPanes(){
        List<TitledPane> notificationPanes = new ArrayList<>();

        for (NotificationsDTO.NotificationDTO notification: notifications){
            Label textLabel = createNotificationTextLabel(notification);
            TitledPane newPane = new TitledPane(notification.dateTime, textLabel);
            notificationPanes.add(newPane);
        }

        return notificationPanes;
    }

    private Label createNotificationTextLabel(NotificationsDTO.NotificationDTO notification){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Yaz to make payment: ");
        stringBuilder.append(notification.yaz);
        stringBuilder.append('\n');
        stringBuilder.append("Loan name: ");
        stringBuilder.append(notification.loanName);
        stringBuilder.append('\n');
        stringBuilder.append("Amount to pay: ");
        stringBuilder.append(notification.amount);
        stringBuilder.append('\n');

        return new Label(stringBuilder.toString());
    }

    public void updateNotifications(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/notifications").newBuilder();
        urlBuilder.addQueryParameter("customer-name", parentController.getCustomerNameProperty().getValue());
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder().url(finalUrl).get().build();
        Call call = Configurations.HTTP_CLIENT.newCall(request);

        Callback updateNotificationsCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    parentController.createExceptionDialog(e);
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String rawBody = response.body().string();
                boolean isResponseSuccessful = response.isSuccessful();
                int responseCode = response.code();
                response.close();

                if(isResponseSuccessful){
                    Platform.runLater(() -> {
                        NotificationsDTO notificationsDTO = Configurations.GSON.fromJson(rawBody, NotificationsDTO.class);
                        notifications.clear();
                        notifications.addAll(notificationsDTO.notifications);
                    });
                }

                else{
                    parentController.createExceptionDialog(new Exception(Integer.toString(responseCode)));
                }
            }
        };

        call.enqueue(updateNotificationsCallBack);
    }

    public void onShow(){
        updateNotifications();
        borrowerLoansTableComponentController.clearTable();
        borrowerLoansTableComponentController.loadSpecificCustomerLoansAsBorrower(customerNameProperty.getValue());
        lendersTableView.getItems().clear();
        payToAllLendersButton.setDisable(true);
        payToLenderButton.setDisable(true);
        closeLoanButton.setDisable(true);
        debtLabel.setText("Debt: ");
    }

    public void LoanWasSelectedFromLoansTable(LoanDTO loanDTO){
        ObservableList<LoanDTO.LenderDetailsDTO> lendersForTable = FXCollections.observableArrayList();

        if(loanDTO.getStatus() != "FINISHED") {
            lendersForTable.addAll((loanDTO).getLenderDTOS());
            lendersTableView.getItems().clear();
            lendersTableView.setItems(lendersForTable);
            closeLoanButton.setDisable(false);
        }

        payDebtTextField.setDisable(!loanDTO.getStatus().equals("IN_RISK"));
        payDebtButton.setDisable(!loanDTO.getStatus().equals("IN_RISK"));
        debtLabel.setText("");
        debtLabel.setText("Debt: " + Double.toString(loanDTO.getDebt()));
        payToAllLendersButton.setDisable(lendersForTable.isEmpty());
    }
}
