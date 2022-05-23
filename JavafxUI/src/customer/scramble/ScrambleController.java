package customer.scramble;

import Engine.MainSystem;
import customer.CustomerController;
import customer.scramble.scrambleFields.simpleField.SimpleField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ScrambleController {

    private CustomerController parentController;
    private MainSystem model;
    private ArrayList<SimpleField> scrambleQueryFields;
    private BooleanProperty isScrambleFormValid;
    private Map<Node, BooleanProperty> formFields2TheirValidity;

    public ScrambleController(){
        scrambleQueryFields = new ArrayList<>();
        isScrambleFormValid = new SimpleBooleanProperty();
        loanCategoriesViewTable = new TableView<String>();
        formFields2TheirValidity = new HashMap<Node, BooleanProperty>();
    }

    public void setParentController(CustomerController parentController){
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
    private TableView<String> loanCategoriesViewTable;

    @FXML
    private Button findLoansButton;

    @FXML
    private ProgressBar findLoansProgressionBar;

    @FXML
    void categoriesRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    void findLoansButtonClicked(ActionEvent event) {

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
    }

    private void initializeTextFields(){
        setMandatoryToggleButtons();
        bindTextFieldsAndRadioButtonSelection();

    }

    private void initializeTableView(){
        setSelectionModelOfTableView();
    }

    private void setSelectionModelOfTableView(){
        loanCategoriesViewTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }

    enum FormCLAZZ {
        TextField,TableView
    }

    private void bindFieldsToTheirValidity(){
        FormCLAZZ typeOfNode;

        for (Node key:formFields2TheirValidity.keySet()){
            typeOfNode = FormCLAZZ.valueOf(key.getClass().getSimpleName());

            switch (typeOfNode){
                case TextField:
                    TextField textFieldKey = (TextField)key;
                    registerValidityPropertyAsListenerToTextField(textFieldKey);

                    break;

                case TableView:
                    TableView<?> tableView = (TableView<?>)key;
                    registerValidityPropertyAsListenerToTableView(tableView);

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
            if(isTextFieldValid(newValue)){
                formFields2TheirValidity.get(textFieldKey).setValue(true);
            }

            else{
                formFields2TheirValidity.get(textFieldKey).setValue(false);
            }
        }));
    }

    private Boolean isTextFieldValid(String text){
        //check regex

        return true;
    }

    private boolean areTableSelectedItemsValid(ObservableList<?> selectedItems){

        return true;
    }

    private void bindTextFieldsAndRadioButtonSelection(){
        minInterestPerYazTextField.disableProperty().bind(minInterestPerYazRatioButton.selectedProperty().not());
        minInterestPerYazTextField.disableProperty().bind(minInterestPerYazRatioButton.selectedProperty().not());
        minYazForLoanTextField.disableProperty().bind(minYazForLoanRadioButton.selectedProperty().not());
        maxOpenLoansForBorrowerTextField.disableProperty().bind(maxOpenLoansForBorrowerRadioButton.selectedProperty().not());
        maxPercentageOwnershipTextField.disableProperty().bind(maxPercentageOwnershipRadioButton.selectedProperty().not());
        loanCategoriesViewTable.disableProperty().bind(chooseLoanCategoriesRadioButton.selectedProperty().not());

        //form validity


    }

    private void setFields2TheirValidity(){
        formFields2TheirValidity.put(amountTextField,new SimpleBooleanProperty());
        formFields2TheirValidity.put(minInterestPerYazTextField,new SimpleBooleanProperty());
        formFields2TheirValidity.put(minYazForLoanTextField,new SimpleBooleanProperty());
        formFields2TheirValidity.put(maxOpenLoansForBorrowerTextField,new SimpleBooleanProperty());
        formFields2TheirValidity.put(maxPercentageOwnershipTextField,new SimpleBooleanProperty());
        formFields2TheirValidity.put(loanCategoriesViewTable,new SimpleBooleanProperty());
    }

    private void setMandatoryToggleButtons(){
        ToggleGroup mandatoryButtons = new ToggleGroup();
        amountRadioButton.setToggleGroup(mandatoryButtons);
        mandatoryButtons.selectToggle(amountRadioButton);
        amountRadioButton.setFocusTraversable(false);
    }

    public void onShow(){
        //createScrambleFields();
    }




    //create fields dynamically methods ->
//    private void createScrambleFields(){
//        ScrambleQueryFieldsDTO fieldsDTO = this.model.getScrambleQueryFields();
//        int fieldIndex = 1;
//
//        for (ArrayList<String> scrambleQueryField: fieldsDTO.scrambleQueryFields){
//            createField(scrambleQueryField, fieldIndex, fieldsDTO);
//            ++fieldIndex;
//        }
//    }

//    private void createField(ArrayList<String> fieldConfigurations, int rowIndex, ScrambleQueryFieldsDTO fieldsDTO){
//        int colIndexOfSimpleField = 1;
//        String typeOfField = fieldConfigurations.get(2);
//
//        switch (typeOfField){
//            case("field"):
//                addSimpleField(rowIndex, colIndexOfSimpleField, fieldConfigurations);
//                break;
//
//            case("table"):
//                addTableField(fieldConfigurations, fieldsDTO);
//                break;
//        }
//    }

    private void addSimpleField(int rowIndex, int colIndex, ArrayList<String> fieldConfigurations){
        SimpleField newScrambleSimpleQueryField = new SimpleField(fieldConfigurations.get(0), fieldConfigurations.get(1), fieldConfigurations.get(2));
        scrambleQueryFieldsGrid.addRow(rowIndex);
        scrambleQueryFieldsGrid.add(newScrambleSimpleQueryField.radioButton, colIndex, rowIndex);
        scrambleQueryFieldsGrid.add(newScrambleSimpleQueryField.textField, colIndex + 2, rowIndex);
    }

//    private void addTableField(ArrayList<String> fieldConfigurations, ScrambleQueryFieldsDTO fieldsDTO){
//        // need to put all the categoris that came from the DTO in the table....
//
//        TableField newTableField = new TableField(fieldConfigurations.get(0), fieldConfigurations.get(1), fieldConfigurations.get(2));
//        ArrayList<Category> newCategoryList = new ArrayList<>();
//        for (Lo:
//             ) {
//
//        }
////        TableColumn<AccountMovementDTO, Integer> yazCol = new TableColumn<AccountMovementDTO, Integer>("Yaz");
////        yazCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Integer>("yaz"));
////        accountTransactionsTableView.getColumns().add(yazCol);
//
//        TableColumn<TableCategory, String> categoryCol = new TableColumn<TableCategory, String>("Category");
//        categoryCol.setCellValueFactory(new PropertyValueFactory<TableCategory,String>("category"));
//        newTableField.tableView.getColumns().add(categoryCol);
//        newTableField.tableView.setItems();
//
//
//
//    }
}
