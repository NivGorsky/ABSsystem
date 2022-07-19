package adminScene;
import DTO.CustomerDTO;
import DTO.RewindAdminDTO;
import customersInfoTable.CustomersInfoTableController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jsonDeserializer.GsonWrapper;
import loansTable.LoansTableComponentController;
import main.AdminComponentsRefresher;
import main.Configurations;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Timer;

public class AdminSceneController implements ParentController {

    @FXML private GridPane header;
    @FXML private Label currentYazLabel;
    @FXML private ComboBox<String> displayModeCB;
    @FXML private Label heyAdminLabel;

    @FXML private Button increaseYazButton;
    @FXML private Button rewindButton;
    @FXML private ComboBox<Integer> rewindYazChooseCB;
    @FXML private Button loadFileButton;

    @FXML private Label loansLabel;
    @FXML private ScrollPane loansTableComponent;
    @FXML private LoansTableComponentController loansTableComponentController;

    @FXML private Label customersInfoLabel;
    @FXML private TableView<CustomerDTO> customersInfoTableView;
    @FXML private CustomersInfoTableController customersInfoTableController;

    private Timer timer;
    private final int REFRESH_RATE = 2;
    private SimpleStringProperty adminName = new SimpleStringProperty();
    private SimpleIntegerProperty currentYAZ;
    private ParentController parentController;
    private SimpleBooleanProperty isRewindMode = new SimpleBooleanProperty(false);
    private RewindAdminDTO rewindAdminDTO;

    ObservableList<String> displayModeOptions =  FXCollections.observableArrayList("Light Mode", "Dark Mode", "MTA Mode", "Barbi Mode");

    public AdminSceneController(){
        currentYAZ = new SimpleIntegerProperty();
        getCurrentYaz();
    }


    @FXML public void initialize()
    {
        displayModeCB.setItems(displayModeOptions);
        currentYazLabel.textProperty().bind(Bindings.concat("Current YAZ: ", currentYAZ));
        displayModeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            parentController.switchStyleSheet(displayModeCB.getSelectionModel().getSelectedItem());
        });

        if(loansTableComponentController != null && customersInfoTableController != null)
        {
            loansTableComponentController.setParentController(this);
            customersInfoTableController.setParentController(this);
        }

        rewindYazChooseCB.disableProperty().bind(isRewindMode.not());
        rewindYazChooseCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(!rewindYazChooseCB.getSelectionModel().isEmpty()){
                sendStartRewindRequest(rewindYazChooseCB.getSelectionModel().getSelectedItem().intValue());
            }
        });
        startRefresher();
    }

    public void setAdminName(String adminName) {
        this.adminName.set(adminName);
    }

    public String getAdminName() {
        return adminName.get();
    }

    public void setAdmin(String name) {
        adminName.set(name);
        heyAdminLabel.setText("Hey " + name + "!");
    }

    @FXML public void increaseYazButtonClicked() {
        Request request = createCurrentYazRequest("+");
        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback currentYazCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int responseCode = response.code();
                boolean isResponseSuccessful = response.isSuccessful();
                String responseBody = response.body().string();
                response.close();

                if (!isResponseSuccessful) {
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(responseCode + "\n" + responseBody)));
                }
                else {
                    Platform.runLater(() -> {
                        currentYAZ.set(Integer.parseInt(responseBody));
                    });
                }
            }
        };

        call.enqueue(currentYazCallBack);
    }

    @FXML public void rewindButtonClicked() {
        if(isRewindMode.get() == false) {
            isRewindMode.set(true);
            rewindButton.setText("Finish Rewind");

            ObservableList<Integer> yaz = FXCollections.observableArrayList();
            for (int i=1; i<=currentYAZ.get(); i++) {
                yaz.add(i);
            }

            rewindYazChooseCB.setItems(yaz);
        }
        else {
            isRewindMode.set(false);
            rewindButton.setText("Rewind");
            rewindYazChooseCB.getSelectionModel().clearSelection();
            getCurrentYaz();
            sendEndRewindRequest();
        }
    }

    private void sendStartRewindRequest(int chosenYaz) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/rewind").newBuilder();
        urlBuilder.addQueryParameter("operation", "start");
        urlBuilder.addQueryParameter("yaz-rewind", String.valueOf(chosenYaz));
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder().url(finalUrl).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback currentYazCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                int responseCode = response.code();
                response.close();

                if (responseCode != 200) {
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(body)));
                }
                else {
                    Platform.runLater(() -> {
                        onShowRewind();
                    });
                }
            }
        };

        call.enqueue(currentYazCallBack);
    }

    private void sendEndRewindRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/rewind").newBuilder();
        urlBuilder.addQueryParameter("operation", "end");
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder().url(finalUrl).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback currentYazCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                int responseCode = response.code();
                response.close();

                if (responseCode != 200) {
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(body)));
                }
                else {
                    Platform.runLater(() -> {
                        onShow();
                    });
                }
            }
        };

        call.enqueue(currentYazCallBack);
    }

    private void onShowRewind(){
        getRewindDTO();

        if(rewindAdminDTO != null){
            //load loans info
            loansTableComponentController.clearTable();
            loansTableComponentController.putLoansInTable(rewindAdminDTO.getLoans());

            //load customers info
            customersInfoTableController.clearTable();
            customersInfoTableController.loadCustomers(rewindAdminDTO.getCustomers());

            //update the yaz
            currentYAZ.set(rewindAdminDTO.getCurrentYaz());
        }
    }

    private void getRewindDTO(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/getRewindData").newBuilder();
        urlBuilder.addQueryParameter("consumer", "ADMIN");
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder().url(finalUrl).build();
        Call call = Configurations.HTTP_CLIENT.newCall(request);
        executeRequestSynchronized(call);
    }

    private void executeRequestSynchronized(Call call){
        try{
            Response response = call.execute();
            String responseBodyAsJson = response.body().string();
            int responseCode = response.code();
            response.close();

            if (responseCode != 200) {
                Platform.runLater(() ->
                        parentController.createExceptionDialog(new Exception()));
            }

            else{
                rewindAdminDTO = GsonWrapper.GSON.fromJson(responseBodyAsJson, RewindAdminDTO.class);
            }
        }

        catch (Exception e){
            System.out.println(e.getMessage());

        }

    }

    public void setParentController(ParentController parentController)
    {
        this.parentController = parentController;
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
        return adminName.get();
    }

    public void onShow(){
        getCurrentYaz();
        loansTableComponentController.clearTable();
        loansTableComponentController.loadLoansData();
        customersInfoTableController.clearTable();
        customersInfoTableController.loadCustomersInfo();
    }

    public void startRefresher() {
        AdminComponentsRefresher refresher = new AdminComponentsRefresher(this);
        timer = new Timer();
        timer.schedule(refresher, REFRESH_RATE, REFRESH_RATE);
    }

    public Request createCurrentYazRequest(String moveDirection) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/currentYaz").newBuilder();
        urlBuilder.addQueryParameter("move-direction", moveDirection);
        String finalUrl = urlBuilder.build().toString();

        return new Request.Builder().url(finalUrl).build();

    }

    public void getCurrentYaz(){
        Request request = createCurrentYazRequest("=");
        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback currentYazCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int responseCode = response.code();
                boolean isResponseSuccessful = response.isSuccessful();
                String responseBody = response.body().string();
                response.close();

                if (!isResponseSuccessful) {
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(responseCode + "\n" + responseBody)));
                }
                else {
                    Platform.runLater(() -> {
                        currentYAZ.set(Integer.parseInt(responseBody));
                    });
                }
            }
        };

        call.enqueue(currentYazCallBack);

    }

}


