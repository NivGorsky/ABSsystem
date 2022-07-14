package customerScene.payment;

import DTO.LoanDTO;
import DTO.NotificationsDTO;
import Engine.MainSystem;
import customerScene.CustomerSceneController;
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
import mutualInterfaces.ParentController;

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

    @FXML
    void payDebtButtonClicked(ActionEvent event){
        double amount = Double.parseDouble(payDebtTextField.getText());

        try{
            parentController.getModel().payDebt(amount, selectedLoanFromLoansTable, parentController.getModel().getCurrYaz());
        }

        catch (Exception e){
            parentController.createExceptionDialog(e);
        }
    }

    @FXML
    void closeLoanButtonClicked(ActionEvent event) {
        double loanAmountToPay = selectedLoanFromLoansTable.getDebt();
        String customerName = this.customerNameProperty.getValue();
        try {
            if(parentController.getModel().getCustomerDTO(customerName).getBalance() >= loanAmountToPay){
                parentController.getModel().closeLoan(selectedLoanFromLoansTable, parentController.getModel().getCurrYaz());
            }
            else{
                throw new Exception("Insufficient funds to pay the loan");
            }
        }
        catch (Exception ex) {
           parentController.createExceptionDialog(ex);
        }
    }

    @FXML
    void payToAllLendersButtonClicked(ActionEvent event) {
        try {
            parentController.getModel().payToAllLendersForCurrentYaz(selectedLoanFromLoansTable,parentController.getModel().getCurrYaz());
        }
        catch (Exception ex) {
            parentController.createExceptionDialog(ex);
        }
    }

    @FXML
    void payToLenderButtonClicked(ActionEvent event) {
        try {
            parentController.getModel().payToLender(lendersTableView.getSelectionModel().getSelectedItem(), selectedLoanFromLoansTable , parentController.getModel().getCurrYaz());
        }
        catch (Exception ex) {
            parentController.createExceptionDialog(ex);
        }
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

    @Override
    public MainSystem getModel(){
        return parentController.getModel();
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
        NotificationsDTO notificationsDTO = parentController.getModel().getNotificationsDTO(customerNameProperty.getValue());
        notifications.clear();
        notifications.addAll(notificationsDTO.notifications);
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
