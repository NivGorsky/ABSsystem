package customer.scramble.scrambleFields.tableField;

import customer.scramble.scrambleFields.ScrambleQueryField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class TableField extends ScrambleQueryField {
    public TableView<TableCategory> tableView;
    public ObservableList<TableCategory> tableItems;

    public TableField(String name, String isMandatory, String type){
        super(name, isMandatory, type);
        tableView = new TableView<>();
        tableItems = FXCollections.observableArrayList();
    }
}
