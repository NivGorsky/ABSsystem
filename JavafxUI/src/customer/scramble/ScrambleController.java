package customer.scramble;

import DTO.AccountMovementDTO;
import DTO.ScrambleQueryFieldsDTO;
import Engine.MainSystem;
import customer.CustomerController;
import customer.scramble.scrambleFields.ScrambleQueryField;
import customer.scramble.scrambleFields.simpleField.SimpleField;
import customer.scramble.scrambleFields.tableField.TableCategory;
import customer.scramble.scrambleFields.tableField.TableField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import jdk.jfr.Category;

import java.util.ArrayList;

public class ScrambleController {

    private CustomerController parentController;
    private MainSystem model;
    private ArrayList<SimpleField> scrambleQueryFields;

    public ScrambleController(){
        scrambleQueryFields = new ArrayList<>();
    }

    public void setParentController(CustomerController parentController){
        this.parentController = parentController;
    }

    public void setModel(MainSystem model){
        this.model = model;
    }
    @FXML
    private RadioButton minInterestRatioButton;

    @FXML
    private TextField minInterestTextField;

    @FXML
    private GridPane scrambleQueryFieldsGrid;

    @FXML
    private TextField maxPercentageOwnershipTextField;

    @FXML
    private TextField minYazTextField;

    @FXML
    private TextField maxOpenLoansTextField;

    @FXML
    private RadioButton minYazRadioButton;

    @FXML
    private RadioButton maxOpenLoansRadioButton;

    @FXML
    private RadioButton maxPercentageOwnershipRadioButton;

    @FXML
    private RadioButton amountRadioButton;

    @FXML
    private TextField amountTextField;

    @FXML
    private RadioButton categoriesRadioButton;

    @FXML
    private TableView<?> loanCategoriesViewTable;

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
    void maxPercentageOwnershipRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    void minInterestRadioButtonClicked(ActionEvent event) {

    }

    @FXML
    void minYazRadioButtonClicked(ActionEvent event) {

    }

    public void onShow(){
        createScrambleFields();
    }

    private void createScrambleFields(){
        ScrambleQueryFieldsDTO fieldsDTO = this.model.getScrambleQueryFields();
        int index = 1;

        for (ArrayList<String> scrambleQueryField: fieldsDTO.scrambleQueryFields){
            createField(scrambleQueryField, index, fieldsDTO);
            ++index;
        }
    }

    private void createField(ArrayList<String> fieldConfigurations, int rowIndex, ScrambleQueryFieldsDTO fieldsDTO){
        int colIndexOfSimpleField = 1;
        String typeOfField = fieldConfigurations.get(2);

        switch (typeOfField){
            case("field"):
                addSimpleField(rowIndex, colIndexOfSimpleField, fieldConfigurations);
                break;

            case("table"):
                addTableField(fieldConfigurations, fieldsDTO);
                break;
        }
    }

    private void addSimpleField(int rowIndex, int colIndex, ArrayList<String> fieldConfigurations){
        SimpleField newScrambleSimpleQueryField = new SimpleField(fieldConfigurations.get(0), fieldConfigurations.get(1), fieldConfigurations.get(2));
        scrambleQueryFieldsGrid.addRow(rowIndex);
        scrambleQueryFieldsGrid.add(newScrambleSimpleQueryField.radioButton, colIndex, rowIndex);
        scrambleQueryFieldsGrid.add(newScrambleSimpleQueryField.textField, colIndex + 2, rowIndex);
    }

    private void addTableField(ArrayList<String> fieldConfigurations, ScrambleQueryFieldsDTO fieldsDTO){
        // need to put all the categoris that came from the DTO in the table....

        TableField newTableField = new TableField(fieldConfigurations.get(0), fieldConfigurations.get(1), fieldConfigurations.get(2));
        ArrayList<Category> newCategoryList = new ArrayList<>();
        for (Lo:
             ) {
            
        }
//        TableColumn<AccountMovementDTO, Integer> yazCol = new TableColumn<AccountMovementDTO, Integer>("Yaz");
//        yazCol.setCellValueFactory(new PropertyValueFactory<AccountMovementDTO, Integer>("yaz"));
//        accountTransactionsTableView.getColumns().add(yazCol);
        TableColumn<TableCategory, String> categoryCol = new TableColumn<TableCategory, String>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<TableCategory,String>("category"));
        newTableField.tableView.getColumns().add(categoryCol);
        newTableField.tableView.setItems();



    }
}
