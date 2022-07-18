package customerScene;
import DTO.CustomerDTO;
import DTO.RewindCustomerDTO;
import customerScene.createLoanScene.CreateLoanSceneController;
import customerScene.information.InformationController;
import customerScene.loanTrading.LoanTradingSceneController;
import customerScene.payment.PaymentController;
import customerScene.scramble.ScrambleController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jsonDeserializer.GsonWrapper;
import main.Configurations;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

import main.CustomerComponentsRefresher;


public class CustomerSceneController implements ParentController {


    //header//
    @FXML private GridPane header;
    @FXML private Button loadFileButton;
    @FXML private Label currentYazLabel;
    @FXML private ComboBox<String> displayModeCB;
    @FXML private Label heyCustomerLabel;

    //.............................................................................................//

    //information tab
    @FXML private ScrollPane information;
    @FXML private InformationController informationController;
    @FXML private Tab informationTab;

    //.............................................................................................//

    //scramble tab
    @FXML private ScrollPane scramble;
    @FXML private ScrambleController scrambleController;
    @FXML private Tab scrambleTab;

    //.............................................................................................//

    //payment tab
    @FXML private ScrollPane payment;
    @FXML private PaymentController paymentController;
    @FXML private Tab paymentTab;

    //.............................................................................................//

    //create new loan tab
    @FXML private  ScrollPane createNewLoan;
    @FXML private CreateLoanSceneController createNewLoanController;
    @FXML private Tab createNewLoanTab;

    //.............................................................................................//

    //loans trading tab
    @FXML private  ScrollPane loansTrading;
    @FXML private LoanTradingSceneController loansTradingController;
    @FXML private Tab loanTradingTab;

    //.............................................................................................//

    @FXML private TabPane customerTabPane;

    private SimpleBooleanProperty isFileSelected;
    private SimpleIntegerProperty currentYAZ;
    private SimpleBooleanProperty isRewindMode;

    ObservableList<String> displayModeOptions =  FXCollections.observableArrayList("Light Mode", "Dark Mode", "MTA Mode", "Barbi Mode");

    private ParentController parentController;
    private StringProperty customerNameProperty;

    //.............................................................................................//

    private Timer timer;
    private final int REFRESH_RATE = 2;
    private RewindCustomerDTO rewindCustomerDTO;

    //.............................................................................................//


    public CustomerSceneController(){
        customerNameProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        currentYAZ = new SimpleIntegerProperty(1);
        isRewindMode = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize() {
        displayModeCB.setItems(displayModeOptions);
        currentYazLabel.textProperty().bind(Bindings.concat("Current YAZ: ", currentYAZ));
        currentYazLabel.disableProperty().bind(isFileSelected.not());
        displayModeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            parentController.switchStyleSheet(displayModeCB.getSelectionModel().getSelectedItem());
        });

        informationController.setParentController(this);
        scrambleController.setParentController(this);
        paymentController.setParentController(this);
        loansTradingController.setParentController(this);
        createNewLoanController.setParentController(this);

        createNewLoanTab.disableProperty().bind(isRewindMode);
        scrambleTab.disableProperty().bind(isRewindMode);
        paymentTab.disableProperty().bind(isRewindMode);
        loanTradingTab.disableProperty().bind(isRewindMode);
        loadFileButton.disableProperty().bind(isRewindMode);
        informationController.getChargeButton().disableProperty().bind(isRewindMode);
        informationController.getWithdrawButton().disableProperty().bind(isRewindMode);
    }

    public SimpleBooleanProperty getIsRewindMode() { return isRewindMode; }

    private boolean areControllersInitialized(InformationController customerController, ScrambleController scrambleController, PaymentController paymentController){

        return customerController != null && scrambleController != null && paymentController != null;
    }

    public StringProperty getCustomerNameProperty(){
        return customerNameProperty;
    }
    public IntegerProperty getCurrentYazProperty(){return currentYAZ;}

    public void setParentController(ParentController parentController){
        this.parentController = parentController;
    }

    public void initializeTabs() {
        //init information
        informationController.setParentController(this);
        informationController.getCustomerNameProperty().bind(customerNameProperty);
        informationController.onShow();

        //init scramble
        scrambleController.setParentController(this);

        //init payment
        paymentController.setParentController(this);
        paymentController.getCustomerNameProperty().bind(customerNameProperty);

        if (areControllersInitialized(informationController, scrambleController, paymentController)){
            customerTabPane.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
                switch (newValue.getText()){
                    case "Information":
                        informationController.onShow();
                        break;
                    case "Scramble":
                        scrambleController.onShow();
                        break;
                    case "Payment":
                        paymentController.onShow();
                        break;
                    case "Create new loan":
                        createNewLoanController.onShow();
                        break;
                    case "Loans trading":
                        loansTradingController.onShow();
                        break;
                }
            }));
        }
    }

    public TabPane getCustomerTabPane() {
        return customerTabPane;
    }

    public void chooseTab(int tabIndex){
        customerTabPane.getSelectionModel().select(1);
        customerTabPane.getSelectionModel().select(0);
    }

    @Override
    public void createExceptionDialog(Exception ex) {
        parentController.createExceptionDialog(ex);
    }

   @Override
    public Stage getPrimaryStage() { return parentController.getPrimaryStage(); }

    @Override
    public void switchStyleSheet(String selectedItem) {
        parentController.switchStyleSheet(selectedItem);
    }

    @Override
    public String getLoggedInUser() {
        return customerNameProperty.get();
    }

    @FXML
    public void loadFileButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(parentController.getPrimaryStage());


        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/upload-file").newBuilder();
        urlBuilder.addQueryParameter("customer-name", customerNameProperty.getValue());
        String finalUrl = urlBuilder.build().toString();

        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", selectedFile.getName(), RequestBody.create(selectedFile, MediaType.parse("text/plain")))
                        .build();

        Request request = new Request.Builder().url(finalUrl).post(body).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);

        Callback fileCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int responseCode = response.code();
                boolean isResponseSuccessful = response.isSuccessful();
                response.close();


                if (!isResponseSuccessful) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(responseBody)));
                }
                else {
                    Platform.runLater(() -> {
                        isFileSelected.set(true);
                        onShow();
                    });
                }
           }
        };

        call.enqueue(fileCallBack);

    }

    public void setCustomer(String name) {
        customerNameProperty.set(name);
        informationController.setCustomerNameProperty(name);
        heyCustomerLabel.setText("Hey " + name + "!");

    }

    public void updateCurrentYaz() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/currentYaz").newBuilder();
        urlBuilder.addQueryParameter("move-direction", "=");
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
                String responseBody = response.body().string();
                int responseCode = response.code();
                response.close();

                if (responseCode != 200) {
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(responseBody)));
                }

                else{
                    Platform.runLater(() -> {
                        try {
                            currentYAZ.set(Integer.parseInt(responseBody));
                        }
                        catch (Exception e) {
                            parentController.createExceptionDialog(e);
                        }

                    });
                }
            }
        };

        call.enqueue(currentYazCallBack);
    }

    private void onShowRewind(){
        getRewindDTO();

        if(rewindCustomerDTO != null){
            informationController.getBorrowerLoansTableComponentController().clearTable();
            informationController.getBorrowerLoansTableComponentController().putLoansInTable(rewindCustomerDTO.getCustomerDTO().getLoansAsBorrower());

            informationController.getLenderLoansTableComponentController().clearTable();
            informationController.getLenderLoansTableComponentController().putLoansInTable(rewindCustomerDTO.getCustomerDTO().getLoansAsLender());


            informationController.getAccountTransactionsController().updateAccountMovementsTable(rewindCustomerDTO.getCustomerDTO().getAccountMovements());
            currentYAZ.set(rewindCustomerDTO.getYaz());
        }
    }

    private void getRewindDTO(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/getRewindData").newBuilder();
        urlBuilder.addQueryParameter("consumer", "customer");
        urlBuilder.addQueryParameter("customerName", customerNameProperty.getValue());
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder().url(finalUrl).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback rewindCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBodyAsJson = Configurations.GSON.fromJson(response.body().string(), String.class);
                int responseCode = response.code();
                response.close();

                if (responseCode != 200) {
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception()));
                }

                else{
                    Platform.runLater(() -> {
                        rewindCustomerDTO = GsonWrapper.GSON.fromJson(responseBodyAsJson, RewindCustomerDTO.class);
                    });
                }
            }
        };

        call.enqueue(rewindCallBack);
    }

    public void onShow() {
        updateCurrentYaz();
        updateIsRewindMode();

        if(isFileSelected.get())
        {
            informationController.onShow();
            paymentController.onShow();
            loansTradingController.onShow();
            createNewLoanController.onShow();
        }
    }

    private void updateIsRewindMode() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/isRewindMode").newBuilder();
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder().url(finalUrl).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback rewindCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = Configurations.GSON.fromJson(response.body().string(), String.class);
                int responseCode = response.code();
                response.close();

                if (responseCode != 200) {
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception()));
                }

                else{
                    Platform.runLater(() -> {
                        isRewindMode.set(Boolean.parseBoolean(responseBody));
                        if(isRewindMode.getValue()){
                            onShowRewind();
                        }
                    });
                }
            }
        };

        call.enqueue(rewindCallBack);
    }

    public void startRefresher() {
        CustomerComponentsRefresher refresher = new CustomerComponentsRefresher(this);
        timer = new Timer();
        timer.schedule(refresher, REFRESH_RATE, REFRESH_RATE);
    }

    public boolean getIsFileSelected() { return isFileSelected.get(); }
}


