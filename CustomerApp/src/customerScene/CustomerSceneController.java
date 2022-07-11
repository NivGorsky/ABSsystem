package customerScene;
import Engine.MainSystem;
import Exceptions.XMLFileException;
import customerScene.information.InformationController;
import customerScene.payment.PaymentController;
import customerScene.scramble.ScrambleController;
import exceptionDialog.ExceptionDialogCreator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Configurations;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;


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


    @FXML private TabPane customerTabPane;

    private SimpleBooleanProperty isFileSelected;
    private SimpleIntegerProperty currentYAZ;

    ObservableList<String> displayModeOptions =  FXCollections.observableArrayList("Light Mode", "Dark Mode", "MTA Mode", "Barbi Mode");

    private ParentController parentController;
    private StringProperty customerNameProperty;

    //.............................................................................................//


    public CustomerSceneController(){
        customerNameProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        currentYAZ = new SimpleIntegerProperty(1);
    }

    @FXML public void initialize() {
        displayModeCB.setItems(displayModeOptions);
        currentYazLabel.textProperty().bind(Bindings.concat("Current YAZ: ", currentYAZ));
        currentYazLabel.disableProperty().bind(isFileSelected.not());
        displayModeCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            parentController.switchStyleSheet(displayModeCB.getSelectionModel().getSelectedItem());
        });
    }

    private boolean areControllersInitialized(InformationController customerController, ScrambleController scrambleController, PaymentController paymentController){

        return customerController != null && scrambleController != null && paymentController != null;
    }

    public StringProperty getCustomerNameProperty(){
        return customerNameProperty;
    }

    public void setParentController(ParentController parentController){
        this.parentController = parentController;
    }

    public void initializeTabs() {
        //init information
        informationController.setParentController(this);
        informationController.getCustomerNameProperty().bind(customerNameProperty);

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
    public MainSystem getModel() {
        return parentController.getModel();
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

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);

        Callback fileCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(responseBody)));
                }
                else {
                    Platform.runLater(() -> {
                        initializeTabs();
                        isFileSelected.set(true);
                    });
                }
           }
        };

        call.enqueue(fileCallBack);

    }

    public void setCustomer(String name) {
        customerNameProperty.set(name);
        heyCustomerLabel.setText("Hey " + name + "!");
    }
}

