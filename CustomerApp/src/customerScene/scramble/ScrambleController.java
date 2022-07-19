package customerScene.scramble;
import DTO.LoanCategoriesDTO;
import DTO.LoanPlacingDTO;
import DTO.UIController;
import customerScene.CustomerSceneController;
import customerScene.scramble.scrambleFields.simpleField.SimpleField;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import main.Configurations;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.*;
import java.util.*;

public class ScrambleController implements UIController {

    private ParentController parentController;
    private ArrayList<SimpleField> scrambleQueryFields;
    private BooleanProperty isScrambleFormValid;
    private Map<Node, BooleanProperty> formFields2TheirValidity;
    private IntegerProperty numberOfLoansPlaced;

    public class LoanCategoryForTable {
        private final SimpleStringProperty category;

        public LoanCategoryForTable (String name){
            category = new SimpleStringProperty(name);
        }

        public SimpleStringProperty getCategoryProperty(){
            return this.category;
        }
    }

    public ScrambleController(){
        scrambleQueryFields = new ArrayList<>();
        isScrambleFormValid = new SimpleBooleanProperty();
        loanCategoriesViewTable = new TableView<>();
        formFields2TheirValidity = new HashMap<Node, BooleanProperty>();
        numberOfLoansPlaced = new SimpleIntegerProperty(0);
    }

    public void setParentController(ParentController parentController){
        this.parentController = parentController;
    }

    @FXML private RadioButton minInterestPerYazRatioButton;
    @FXML private TextField minInterestPerYazTextField;
    @FXML private GridPane scrambleQueryFieldsGrid;
    @FXML private TextField maxPercentageOwnershipTextField;
    @FXML private TextField minYazForLoanTextField;
    @FXML private TextField maxOpenLoansForBorrowerTextField;
    @FXML private RadioButton minYazForLoanRadioButton;
    @FXML private RadioButton maxOpenLoansForBorrowerRadioButton;
    @FXML private RadioButton maxPercentageOwnershipRadioButton;
    @FXML private RadioButton amountRadioButton;
    @FXML private TextField amountTextField;
    @FXML private RadioButton chooseLoanCategoriesRadioButton;
    @FXML private TableView<LoanCategoryForTable> loanCategoriesViewTable;
    @FXML private TableColumn<LoanCategoryForTable, String> loanCategoryViewColumn;
    @FXML private Button findLoansButton;
    @FXML private ProgressBar findLoansProgressionBar;
    @FXML private Label progressBarMessageLabel;

    @FXML void categoriesRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    void findLoansButtonClicked(ActionEvent event) {
        placeToLoans();
        clearTextFields();
    }



    private void clearTextFields() {
        amountTextField.clear();
        minInterestPerYazTextField.clear();
        minYazForLoanTextField.clear();
        maxOpenLoansForBorrowerTextField.clear();
        maxPercentageOwnershipTextField.clear();
    }

    @FXML
    void loanCategoriesViewTableSortClicked(ActionEvent event) {

    }

    @FXML
    void maxOpenLoansRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    public void initialize(){
        initializeTextFields();
        initializeTableView();
        setFields2TheirValidity();
        bindFieldsToTheirValidity();
        bindIsScrambleFormValidPropertyToAllFieldsProperties();
        bindScrambleButtonToIsFormValid();
    }

    private void placeToLoans(){
        try {
            double amountToInvest = Double.parseDouble(amountTextField.getText());
            ArrayList<String> categoriesWillingToInvestIn = convertTableDataToStrings();
            double minimumInterestPerYaz = getTextFieldContentToDouble(minInterestPerYazTextField);
            int minimumYazForLoan = getTextFieldContentToInt(minYazForLoanTextField);
            int maximumPercentOwnership = getTextFieldContentToInt(maxPercentageOwnershipTextField);
            int maximumOpenLoansForBorrower = getTextFieldContentToInt(maxOpenLoansForBorrowerTextField);
            String customerName = "";

            if (parentController instanceof CustomerSceneController){
                customerName = ((CustomerSceneController)parentController).getCustomerNameProperty().getValue();
                LoanPlacingDTO loanPlacingDTO = new LoanPlacingDTO(amountToInvest, categoriesWillingToInvestIn, minimumInterestPerYaz, minimumYazForLoan, maximumPercentOwnership, maximumOpenLoansForBorrower, customerName);
                //parentController.getModel().assignLoansToLender(loanPlacingDTO);
//                parentController.getModel().assignLoansToLenderWithTask(loanPlacingDTO, numberOfLoansPlaced::set);


                postPlaceToLoans(loanPlacingDTO);
            }

            else{
                throw new Exception("Parent controller of scramble controller is not Customer controller, can not retrive customer's name");
            }
        }

        catch (Exception e){
            parentController.createExceptionDialog(new Exception("Could not perform place to loans"));
        }
    }

    private void postPlaceToLoans(LoanPlacingDTO loanPlacingDTO){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/loan-placing").newBuilder();
        String finalUrl = urlBuilder.build().toString();
        String loanPlacingDTOAsJson = Configurations.GSON.toJson(loanPlacingDTO);

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(loanPlacingDTOAsJson.getBytes()))
                .build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback loanPlacingCallBack = new Callback() {

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
                            parentController.createExceptionDialog(new Exception(responseCode
                                    + "\n" + responseBody)));
                }

                else{
                    Platform.runLater(() -> {
                        findLoansProgressionBar.progressProperty().setValue(1);
                        findLoansButton.textProperty().set("finished!");
                    });
                }
            }
        };

        call.enqueue(loanPlacingCallBack);
    }

    private Double getTextFieldContentToDouble(TextField textField){
        Double value = -1.0;

        try {
            value = textField.disableProperty().getValue() ? -1 : Double.parseDouble(textField.getText());
        }

        catch (Exception e) {
            parentController.createExceptionDialog(new Exception("Could not convert text field to value"));
        }

        return value;
    }

    private int getTextFieldContentToInt(TextField textField){
        int value = -1;

        try {
            value = textField.disableProperty().getValue() ? -1 : Integer.parseInt(textField.getText());
        }

        catch (Exception e){
            parentController.createExceptionDialog(new Exception("Could not convert text field to value"));
        }

        return value;
    }

    private ArrayList<String> convertTableDataToStrings(){
        ArrayList<String> categoriesAsStrings = new ArrayList<>();

        if(!chooseLoanCategoriesRadioButton.isSelected()){
            convertAllTableDataToStrings(categoriesAsStrings);
        }

        else{
            convertOnlyChosenTableDataToStrings(categoriesAsStrings);
        }

        return categoriesAsStrings;
    }

    private void convertAllTableDataToStrings(ArrayList<String> categoriesAsStrings){
        for (LoanCategoryForTable categoryFromTable:loanCategoriesViewTable.getItems()){
            categoriesAsStrings.add(categoryFromTable.getCategoryProperty().getValue());
        }
    }

    private void convertOnlyChosenTableDataToStrings(ArrayList<String> categoriesAsStrings){
        for (LoanCategoryForTable categoryFromTable:loanCategoriesViewTable.getSelectionModel().getSelectedItems()){
            categoriesAsStrings.add(categoryFromTable.getCategoryProperty().getValue());
        }
    }

    private void bindScrambleButtonToIsFormValid(){
        findLoansButton.disableProperty().bind(isScrambleFormValid.not());
    }

    private void bindIsScrambleFormValidPropertyToAllFieldsProperties(){
        isScrambleFormValid.bind(Bindings.createBooleanBinding(
                ()-> {
                    boolean isValid = true;

                    for (BooleanProperty fieldBooleanProperty:formFields2TheirValidity.values()){
                        if(!fieldBooleanProperty.getValue()){
                            isValid = false;
                            break;
                        }
                    }

                    return isValid;
                },
                formFields2TheirValidity.get(minInterestPerYazTextField),
                formFields2TheirValidity.get(maxPercentageOwnershipTextField),
                formFields2TheirValidity.get(minYazForLoanTextField),
                formFields2TheirValidity.get(maxOpenLoansForBorrowerTextField),
                formFields2TheirValidity.get(amountTextField)
                ));
    }

    private void initializeTextFields(){
        setMandatoryToggleButtons();
        bindTextFieldsAndRadioButtonSelection();
    }

    private void initializeTableView(){
        setSelectionModelOfTableView();
        loanCategoryViewColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryProperty());
    }

    private void setSelectionModelOfTableView(){
        loanCategoriesViewTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }

    enum FieldClazz {
        TextField,TableView
    }

    private void bindFieldsToTheirValidity(){
        FieldClazz typeOfNode;

        for (Node key:formFields2TheirValidity.keySet()){
            typeOfNode = FieldClazz.valueOf(key.getClass().getSimpleName());

            switch (typeOfNode){
                case TextField:
                    registerValidityPropertyAsListenerToTextField((TextField)key);

                    break;

                case TableView:
                    registerValidityPropertyAsListenerToTableView((TableView<?>)key);

                    break;
            }
        }
    }

    private void registerValidityPropertyAsListenerToTableView(TableView<?> tableViewKey){
        ObservableList<?> selectedItems = tableViewKey.getSelectionModel().getSelectedItems();

        selectedItems.addListener((ListChangeListener<Object>) c -> {
            if(areTableSelectedItemsValid(selectedItems)){
                formFields2TheirValidity.get(tableViewKey).setValue(true);
            }

            else{
                formFields2TheirValidity.get(tableViewKey).setValue(false);
            }
        });
    }

    private void registerValidityPropertyAsListenerToTextField(TextField textFieldKey){
        textFieldKey.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(isTextFieldValid(newValue, textFieldKey)){
                formFields2TheirValidity.get(textFieldKey).setValue(true);
            }

            else{
                formFields2TheirValidity.get(textFieldKey).setValue(false);
            }
        }));
    }

    private Boolean isTextFieldValid(String text, TextField textField){
        return (Pattern.matches("[0-9]+",text)) || (textField.disableProperty().getValue());
        }

    private boolean areTableSelectedItemsValid(ObservableList<?> selectedItems){

        return true;
    }

    private void bindTextFieldsAndRadioButtonSelection(){
        minInterestPerYazTextField.disableProperty().bind(minInterestPerYazRatioButton.selectedProperty().not());
        minInterestPerYazTextField.disableProperty().addListener(((observable, oldValue, newValue) -> {
            minInterestPerYazTextField.textProperty().setValue("");
            formFields2TheirValidity.get(minInterestPerYazTextField).setValue(newValue);
        }));

        minYazForLoanTextField.disableProperty().bind(minYazForLoanRadioButton.selectedProperty().not());
        minYazForLoanTextField.disableProperty().addListener(((observable, oldValue, newValue) -> {
            minYazForLoanTextField.textProperty().setValue("");
            formFields2TheirValidity.get(minYazForLoanTextField).setValue(newValue);
        }));

        maxOpenLoansForBorrowerTextField.disableProperty().bind(maxOpenLoansForBorrowerRadioButton.selectedProperty().not());
        maxOpenLoansForBorrowerTextField.disableProperty().addListener(((observable, oldValue, newValue) -> {
            maxOpenLoansForBorrowerTextField.textProperty().setValue("");
            formFields2TheirValidity.get(maxOpenLoansForBorrowerTextField).setValue(newValue);
        }));

        maxPercentageOwnershipTextField.disableProperty().bind(maxPercentageOwnershipRadioButton.selectedProperty().not());
        maxPercentageOwnershipTextField.disableProperty().addListener(((observable, oldValue, newValue) -> {
            maxPercentageOwnershipTextField.textProperty().setValue("");
            formFields2TheirValidity.get(maxPercentageOwnershipTextField).setValue(newValue);
        }));

        loanCategoriesViewTable.disableProperty().bind(chooseLoanCategoriesRadioButton.selectedProperty().not());
        chooseLoanCategoriesRadioButton.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!loanCategoriesViewTable.getSelectionModel().getSelectedItems().isEmpty()){
                loanCategoriesViewTable.getSelectionModel().clearSelection();
            }

            formFields2TheirValidity.get(loanCategoriesViewTable).setValue(newValue);
        }));
    }

    private void setFields2TheirValidity(){
        formFields2TheirValidity.put(amountTextField,new SimpleBooleanProperty(false));
        formFields2TheirValidity.put(minInterestPerYazTextField,new SimpleBooleanProperty(true));
        formFields2TheirValidity.put(minYazForLoanTextField,new SimpleBooleanProperty(true));
        formFields2TheirValidity.put(maxOpenLoansForBorrowerTextField,new SimpleBooleanProperty(true));
        formFields2TheirValidity.put(maxPercentageOwnershipTextField,new SimpleBooleanProperty(true));
        formFields2TheirValidity.put(loanCategoriesViewTable,new SimpleBooleanProperty(true));
    }

    private void setMandatoryToggleButtons(){
        ToggleGroup mandatoryButtons = new ToggleGroup();
        amountRadioButton.setToggleGroup(mandatoryButtons);
        mandatoryButtons.selectToggle(amountRadioButton);
        amountRadioButton.setFocusTraversable(false);
    }

    public void onShow(){
        getLoanCategoriesToTable();
        findLoansButton.setText("Find loans");
        findLoansProgressionBar.progressProperty().setValue(0);
    }

    private void getLoanCategoriesToTable(){
       getCategories();
    }

    private void getCategories() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/categories").newBuilder();
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder().url(finalUrl).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback categoriesCallBack = new Callback() {
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
                    String responseBody = Configurations.GSON.fromJson(body, String.class);
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(Integer.toString(responseCode)
                                    + "\n" + responseBody)));
                }
                else {
                    Platform.runLater(() -> {
                        LoanCategoriesDTO categories = Configurations.GSON.fromJson(body, LoanCategoriesDTO.class);
                        ObservableList<LoanCategoryForTable> observableCategories = FXCollections.observableArrayList();

                        for (String loanCategory: categories.loanCategories){
                            LoanCategoryForTable newLoanCategory = new LoanCategoryForTable(loanCategory);
                            observableCategories.add(newLoanCategory);
                        }

                        loanCategoriesViewTable.setItems(observableCategories);
                    });
                }
            }
        };

        call.enqueue(categoriesCallBack);
    }

    public void bindTaskToUIComponents(Task<Boolean> task){
        progressBarMessageLabel.textProperty().bind(task.messageProperty());
        findLoansProgressionBar.progressProperty().bind(task.progressProperty());
        // task percent label
       findLoansButton.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        task.progressProperty(),
                                        100)),
                        " %"));

        task.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished();
        });
    }

    public void onTaskFinished(){
        progressBarMessageLabel.textProperty().unbind();
        progressBarMessageLabel.textProperty().set("");
        findLoansProgressionBar.progressProperty().unbind();
        findLoansProgressionBar.progressProperty().setValue(0);
        findLoansButton.textProperty().unbind();
        findLoansButton.textProperty().set("Find loans");
        amountTextField.textProperty().set("");
    }
}
