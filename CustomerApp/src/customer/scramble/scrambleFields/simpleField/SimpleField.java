package customer.scramble.scrambleFields.simpleField;

import customer.scramble.scrambleFields.ScrambleQueryField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class SimpleField extends ScrambleQueryField {
    public RadioButton radioButton;
    public TextField textField;

    public SimpleField(String name, String isMandatory, String type){
        super(name,isMandatory,type);

        radioButton = new RadioButton();
        radioButton.setDisable(false);
        radioButton.setText(super.name);
        textField = new TextField();
        textField.setPrefWidth(SimpleFieldConfiguratinos.TEXT_FIELD_WIDTH);
        if(isMandatory.equals("true")){
            radioButton.setDisable(true);
        }
    }
}
