package customerScene.information.accountTransactions;


import DTO.*;

import com.google.gson.reflect.TypeToken;
import customerScene.information.InformationController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Configurations;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class accountTransactionsController {

    //TODO: create the link between this controller's customer name property to the customer controller customer name property.

    private InformationController parentController;
    private StringProperty customerNameProperty;
    private ObservableList<AccountMovementDTO> accountMovements;
    private boolean isModelLoaded;//only if it;s proiperty

    public void setParentController(InformationController parentController){
        this.parentController = parentController;
    }

    public StringProperty getCustomerNameProperty(){return this.customerNameProperty;}

    public void updateAccountMovements(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/account-transactions").newBuilder();
        urlBuilder.addQueryParameter("customer-name", parentController.getCustomerNameProperty().getValue());
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

                        Platform.runLater(() -> {
                            Type arrayListAccountMovementsType = new TypeToken<List<AccountMovementDTO>>(){}.getType();
                            List<AccountMovementDTO> accountMovementDTOS = Configurations.GSON.fromJson(rawBody, arrayListAccountMovementsType);
                            accountMovements.clear();
                            accountMovements.addAll(accountMovementDTOS);
                            accountTransactionsTableView.setItems(accountMovements);
                        });
                    }

                    else{
                        parentController.createExceptionDialog(new Exception(Integer.toString(response.code())));
                    }
                }
            };

            call.enqueue(showLoansInfoCallBack);
    }

    public accountTransactionsController(){
        accountMovements = FXCollections.observableArrayList();
        customerNameProperty = new SimpleStringProperty();
        isModelLoaded = false;
    }

    private void createTableViewColumns(){
        TableColumn<AccountMovementDTO, Integer> yazCol = new TableColumn<AccountMovementDTO, Integer>("Yaz");
        yazCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Integer>("yaz"));
        accountTransactionsTableView.getColumns().add(yazCol);

        TableColumn<AccountMovementDTO, Double> amountCol = new TableColumn<AccountMovementDTO, Double>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Double>("amount"));
        accountTransactionsTableView.getColumns().add(amountCol);

        TableColumn<AccountMovementDTO, Character> movementKindCol = new TableColumn<AccountMovementDTO, Character>("Movement Kind");
        movementKindCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Character>("movementKind"));
        accountTransactionsTableView.getColumns().add(movementKindCol);

        TableColumn<AccountMovementDTO, Double> balanceBeforeCol = new TableColumn<AccountMovementDTO, Double>("Balance Before");
        balanceBeforeCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Double>("balanceBefore"));
        accountTransactionsTableView.getColumns().add(balanceBeforeCol);

        TableColumn<AccountMovementDTO, Double> balanceAfterCol = new TableColumn<AccountMovementDTO, Double>("Balance After");
        balanceAfterCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Double>("balanceAfter"));
        accountTransactionsTableView.getColumns().add(balanceAfterCol);
    }

    @FXML
    public void initialize(){
        try {
            createTableViewColumns();
            customerNameProperty.addListener((observable, oldValue, newValue) -> {
//                updateAccountMovements();
            });
        }

        catch (Exception e){
            System.out.println("Failed to init account transactions controller");
        }
    }

    @FXML private TableView<AccountMovementDTO> accountTransactionsTableView;
}
