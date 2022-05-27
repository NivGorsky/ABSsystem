package customer.payment;

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
import mutualInterfaces.ParentController;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

public class PaymentController implements ParentController {

    //TODO: decided which properties this controller should hold
    //TODO: Maybe create a new component for notification
    //TODO: create the make payment API call

    private CustomerController parentController;
    private MainSystem model;
    private StringProperty customerNameProperty;
    private final ObservableList<NotificationsDTO.NotificationDTO> notifications;

    @FXML ScrollPane borrowerLoansTableComponent;
    @FXML LoansTableComponentController borrowerLoansTableComponentController;
    @FXML private Accordion notificationsBoard;



    @FXML
    private Button payToLenderButton;

    @FXML
    private Button payToAllLendersButton;

    @FXML
    private Button closeLoanButton;

    @FXML
    private TableColumn<?, ?> lendersTableView;

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
        return model;
    }

    public PaymentController(){
        notifications = FXCollections.observableArrayList();
        customerNameProperty = new SimpleStringProperty();
    }
    public StringProperty getCustomerNameProperty(){return this.customerNameProperty;}
    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }
    public void setModel(MainSystem model){this.model = model;}
    public void initialize(){
        borrowerLoansTableComponentController.setParentController(this);
        notificationsBoard.getPanes().clear();
        wireNotificationsListeners();
        //updateNotifications(); //need to check if it's okay since the model isnt yet placed
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
        NotificationsDTO notificationsDTO = model.getNotificationsDTO(customerNameProperty.getValue());
        notifications.clear();
        notifications.addAll(notificationsDTO.notifications);
    }

    public void onShow(){
        updateNotifications();
        borrowerLoansTableComponentController.clearTable();
        borrowerLoansTableComponentController.loadSpecificCustomerLoansAsBorrower(customerNameProperty.getValue());
    }
}
