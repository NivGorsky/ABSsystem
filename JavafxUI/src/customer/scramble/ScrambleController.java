package customer.scramble;

import Engine.MainSystem;
import com.sun.javafx.collections.ObservableListWrapper;
import customer.CustomerController;
import customer.scramble.scrambleFields.simpleField.SimpleField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javax.xml.soap.Text;
import java.util.regex.*;

import java.util.*;
import java.util.concurrent.Callable;

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
        bindIsScrambleFormValidPropertyToAllFieldsProperties();
        bindScrambleButtonToIsFormValid();
    }

    private void bindScrambleButtonToIsFormValid(){
        findLoansButton.disableProperty().bind(isScrambleFormValid.not());
    }

    private void bindIsScrambleFormValidPropertyToAllFieldsProperties(){

//        for (BooleanProperty booleanProperty:formFields2TheirValidity.values()){
//            booleanProperty.bind(Bindings.createBooleanBinding(
//                    () ->{
//                    boolean isValid = true;
//
//                    for (BooleanProperty fieldBooleanProperty:formFields2TheirValidity.values()){
//                        if(!fieldBooleanProperty.getValue()){
//                            isValid = false;
//                            break;
//                        }
//                    }
//
//                    return isValid;
//            },
//            booleanProperty));
//        }

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

//        this.startXProperty().bind(Bindings.createDoubleBinding(
//                () -> {
//                    double slope = (target.getCenterY() - source.getCenterY())/(target.getCenterX() - source.getCenterX());
//                    return source.getCenterX() + Math.cos(Math.atan(slope)) * source.getRadius();
//                },
//                source.centerXProperty(),
//                source.centerYProperty(),
//                target.centerXProperty(),
//                target.centerYProperty(),
//                source.radiusProperty(),
//                ));
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
            loanCategoriesViewTable.getSelectionModel().getSelectedItems().clear();
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
