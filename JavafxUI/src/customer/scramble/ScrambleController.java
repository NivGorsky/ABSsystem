package customer.scramble;
import DTO.LoanCategorisDTO;
import DTO.LoanPlacingDTO;
import Engine.MainSystem;
import customer.CustomerController;
import customer.scramble.scrambleFields.simpleField.SimpleField;
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
import mutualInterfaces.ParentController;
import java.util.regex.*;
import java.util.*;

public class ScrambleController {

    private ParentController parentController;
    private MainSystem model;
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

    public void setModel(MainSystem model){
        this.model = model;
    }
    @FXML
    private RadioButton minInterestPerYazRatioButton;

    @FXML
    private TextField minInterestPerYazTextField;

    @FXML
    private GridPane scrambleQueryFieldsGrid;

    @FXML
    private TextField maxPercentageOwnershipTextField;

    @FXML
    private TextField minYazForLoanTextField;

    @FXML
    private TextField maxOpenLoansForBorrowerTextField;

    @FXML
    private RadioButton minYazForLoanRadioButton;

    @FXML
    private RadioButton maxOpenLoansForBorrowerRadioButton;

    @FXML
    private RadioButton maxPercentageOwnershipRadioButton;

    @FXML
    private RadioButton amountRadioButton;

    @FXML
    private TextField amountTextField;

    @FXML
    private RadioButton chooseLoanCategoriesRadioButton;

    @FXML
    private TableView<LoanCategoryForTable> loanCategoriesViewTable;

    @FXML
    private TableColumn<LoanCategoryForTable, String> loanCategoryViewColumn;

    @FXML
    private Button findLoansButton;

    @FXML
    private ProgressBar findLoansProgressionBar;

    @FXML
    private Label progressBarMessageLabel;

    @FXML
    void categoriesRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    void findLoansButtonClicked(ActionEvent event) {
        if(isAmountForScrambleValid()){
            placeToLoans();
        }

        else{
            parentController.createExceptionDialog(new Exception("Insufficient funds for this scramble request"));
        }
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

    private boolean isAmountForScrambleValid(){
        double amountEnteredToTextField = Double.parseDouble(amountTextField.getText());
        String customerName = ((CustomerController)parentController).getCustomerNameProperty().getValue();

        return parentController.getModel().getCustomerDTO(customerName).getBalance() >= amountEnteredToTextField;
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

            if (parentController instanceof CustomerController){
                customerName = ((CustomerController)parentController).getCustomerNameProperty().getValue();
                LoanPlacingDTO loanPlacingDTO = new LoanPlacingDTO(amountToInvest, categoriesWillingToInvestIn, minimumInterestPerYaz, minimumYazForLoan, maximumPercentOwnership, maximumOpenLoansForBorrower, customerName);
                //parentController.getModel().assignLoansToLender(loanPlacingDTO);
                parentController.getModel().assignLoansToLenderWithTask(loanPlacingDTO, numberOfLoansPlaced::set);
            }

            else{
                throw new Exception("Parent controller of scramble controller is not Customer controller, can not retrive customer's name");
            }
        }

        catch (Exception e){
            //open a message box to user
            System.out.println("Could not perform place to loans");
        }
    }

    private Double getTextFieldContentToDouble(TextField textField){
        Double value = -1.0;

        try {
            value = textField.disableProperty().getValue() ? -1 : Double.parseDouble(textField.getText());
        }

        catch (Exception e){
            System.out.println("Could not convert text field to value");
        }

        return value;
    }

    private int getTextFieldContentToInt(TextField textField){
        int value = -1;

        try {
            value = textField.disableProperty().getValue() ? -1 : Integer.parseInt(textField.getText());
        }

        catch (Exception e){
            System.out.println("Could not convert text field to value");
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
        parentController.getModel().setScrambleController(this);
    }

    private void getLoanCategoriesToTable(){
        LoanCategorisDTO loanCategorisDTO = model.getSystemLoanCategories();
        ObservableList<LoanCategoryForTable> observableCategories = FXCollections.observableArrayList();

        for (String loanCategory:loanCategorisDTO.loanCategories){
            LoanCategoryForTable newLoanCategory = new LoanCategoryForTable(loanCategory);
            observableCategories.add(newLoanCategory);
        }

        loanCategoriesViewTable.setItems(observableCategories);
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

//        // task message
//        taskMessageLabel.textProperty().bind(aTask.messageProperty());
//
//        // task progress bar
//        taskProgressBar.progressProperty().bind(aTask.progressProperty());
//
//        // task percent label
//        progressPercentLabel.textProperty().bind(
//                Bindings.concat(
//                        Bindings.format(
//                                "%.0f",
//                                Bindings.multiply(
//                                        aTask.progressProperty(),
//                                        100)),
//                        " %"));
//
//        // task cleanup upon finish
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
