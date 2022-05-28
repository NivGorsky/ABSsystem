package customer.payment;

import DTO.LoanDTO;
import DTO.NotificationsDTO;
import Engine.MainSystem;
import Engine.Timeline;
import customer.CustomerController;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import loansTable.LoansTableComponentController;
import mutualInterfaces.Delegate;
import mutualInterfaces.ParentController;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

public class PaymentController implements ParentController {

    //TODO: decided which properties this controller should hold
    //TODO: Maybe create a new component for notification
    //TODO: create the make payment API call

    private CustomerController parentController;
    private StringProperty customerNameProperty;
    private final ObservableList<NotificationsDTO.NotificationDTO> notifications;
    private LoanDTO selectedLoanFromLoansTable;
    @FXML ScrollPane borrowerLoansTableComponent;
    @FXML LoansTableComponentController borrowerLoansTableComponentController;
    @FXML private Accordion notificationsBoard;
    @FXML private TableColumn<LoanDTO.LenderDetailsDTO, String> lendersNameTableColumn;
    @FXML private TableColumn<LoanDTO.LenderDetailsDTO, Double> lendersAmountTableColumn;
    @FXML private TableView<LoanDTO.LenderDetailsDTO> lendersTableView;

    @FXML
    private Button payToLenderButton;

    @FXML
    private Button payToAllLendersButton;

    @FXML
    private Button closeLoanButton;

    @FXML
    void closeLoanButtonClicked(ActionEvent event) {

    }

    @FXML
    void payToAllLendersButtonClicked(ActionEvent event) {

    }

    @FXML
    void payToLenderButtonClicked(ActionEvent event) {

    }

    @Override
    public void createExceptionDialog(Exception ex) {
        parentController.createExceptionDialog(ex);
    }

    @Override
    public MainSystem getModel(){
        return parentController.getModel();
    }

    public PaymentController(){
        notifications = FXCollections.observableArrayList();
        customerNameProperty = new SimpleStringProperty();
    }

    public StringProperty getCustomerNameProperty(){return this.customerNameProperty;}

    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }

    public void initialize(){
        borrowerLoansTableComponentController.setParentController(this);
        initNotifications();
        borrowerLoansTableComponentController.changeTableSelectionModelToSingle();
        borrowerLoansTableComponentController.setLoanSelectionListener();
        initLendersTable();
        initPaymentButtons();
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
    }

    public void LoanWasSelectedFromLoansTable(LoanDTO loanDTO){
        ObservableList<LoanDTO.LenderDetailsDTO> lendersForTable = FXCollections.observableArrayList();
        lendersForTable.addAll((loanDTO).getLendersNamesAndAmounts());
        lendersTableView.getItems().clear();
        lendersTableView.setItems(lendersForTable);

        if(lendersForTable.isEmpty()){
            payToAllLendersButton.setDisable(true);
        }

        else{
            payToAllLendersButton.setDisable(false);
        }
    }
}
