package customerScene.information;
import com.sun.media.jfxmedia.events.PlayerEvent;
import customerScene.CustomerSceneController;
import customerScene.information.accountTransactions.accountTransactionsController;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import Engine.MainSystem;
import javafx.stage.Stage;
import loansTable.LoansTableComponentController;
import main.Configurations;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class InformationController implements ParentController
{
    @FXML private ScrollPane accountTransactions;
    @FXML private accountTransactionsController accountTransactionsController;
    @FXML private Button chargeButton;
    @FXML private TextField amountTextField;
    @FXML private Button withdrawButton;
    @FXML private ScrollPane borrowerLoansTableComponent;
    @FXML private LoansTableComponentController borrowerLoansTableComponentController;
    @FXML private ScrollPane lenderLoansTableComponent;
    @FXML private LoansTableComponentController lenderLoansTableComponentController;

    private StringProperty customerNameProperty;
    private CustomerSceneController parentController;
    private SimpleBooleanProperty isTabSelected;

    public InformationController () {
        customerNameProperty = new SimpleStringProperty();

    }
    public void setParentController(CustomerSceneController parentController) {
        this.parentController = parentController;
    }

    public Button getChargeButton() {
        return chargeButton;
    }

    public Button getWithdrawButton() {
        return withdrawButton;
    }

    public void setCustomerNameProperty(String newName){
        customerNameProperty.set(newName);
    }

    public StringProperty getCustomerNameProperty(){return customerNameProperty;}

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
        return null;
    }


    @FXML
    void chargeButtonClicked(ActionEvent event) {
        try {
            if (!amountTextField.getText().isEmpty()){
                String customerName = customerNameProperty.getValue();

                HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/account-transactions").newBuilder();
                urlBuilder.addQueryParameter("operation", "deposit");
                urlBuilder.addQueryParameter("customer-name", customerName);
                urlBuilder.addQueryParameter("amount", amountTextField.getText());
                String finalUrl = urlBuilder.build().toString();

                Request request = new Request.Builder().url(finalUrl).post(RequestBody.create("".getBytes())).build();
                Call call = Configurations.HTTP_CLIENT.newCall(request);
                Callback depositCallBack = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() -> {
                            parentController.createExceptionDialog(e);
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() != 200) {
                            String responseBody = response.body().string();
                            response.close();
                            Platform.runLater(() ->
                                    parentController.createExceptionDialog(new Exception(Integer.toString(response.code())
                                            + "\n" + responseBody)));
                        }

                        else{
                            Platform.runLater(() -> {
                                response.close();
                                amountTextField.clear();
                                accountTransactionsController.updateAccountMovements();
                                amountTextField.clear();
                            });
                        }

                    }
                };
                call.enqueue(depositCallBack);
            }
        }

        catch (Exception ex){
            parentController.createExceptionDialog(ex);
        }
    }

    @FXML
    void withdrawButtonClicked(ActionEvent event) {
        try {
            if (!amountTextField.getText().isEmpty()){
                String customerName = customerNameProperty.getValue();

                HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/account-transactions").newBuilder();
                urlBuilder.addQueryParameter("operation", "withdraw");
                urlBuilder.addQueryParameter("customer-name", customerName);
                urlBuilder.addQueryParameter("amount", amountTextField.getText());
                String finalUrl = urlBuilder.build().toString();

                Request request = new Request.Builder().url(finalUrl).post(RequestBody.create("".getBytes())).build();
                Call call = Configurations.HTTP_CLIENT.newCall(request);
                Callback withdrawCallBack = new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Platform.runLater(() -> {
                            parentController.createExceptionDialog(e);
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.code() != 200) {
                            String responseBody = response.body().string();
                            response.close();
                            Platform.runLater(() ->
                                    parentController.createExceptionDialog(new Exception(Integer.toString(response.code())
                                            + "\n" + responseBody)));
                        }

                        else{
                            Platform.runLater(() -> {
                                response.close();
                                amountTextField.clear();
                                accountTransactionsController.updateAccountMovements();
                                amountTextField.clear();
                            });
                        }
                    }
                };
                call.enqueue(withdrawCallBack);
            }
        }

        catch (Exception ex){
            parentController.createExceptionDialog(ex);
        }
    }

    @FXML
    public void initialize() {
        try {
            if (accountTransactionsController != null) {
                accountTransactionsController.setParentController(this);
                accountTransactionsController.getCustomerNameProperty().bind(this.customerNameProperty);
                borrowerLoansTableComponentController.setParentController(this);
                lenderLoansTableComponentController.setParentController(this);
            }
        }

        catch (Exception e){
            parentController.createExceptionDialog(new Exception("Failed to init information controller"));
        }
    }

    public void onShow(){
        borrowerLoansTableComponentController.clearTable();
        borrowerLoansTableComponentController.loadSpecificCustomerLoansAsBorrower(customerNameProperty.getValue());
        lenderLoansTableComponentController.clearTable();
        lenderLoansTableComponentController.loadSpecificCustomerLoansAsLender(customerNameProperty.getValue());
        accountTransactionsController.updateAccountMovements();
    }
}
