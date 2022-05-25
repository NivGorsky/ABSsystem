package mainScene;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ExceptionDialogCreator
{
    Stage primaryStage;
    ImageView exceptionImg;
    Dialog<Exception> exceptionDialog;

    public ExceptionDialogCreator()
    {
        exceptionDialog = new Dialog<>();
        exceptionDialog.initOwner(primaryStage);
        exceptionDialog.setTitle("Exception!");
        exceptionDialog.setHeight(300);
        exceptionDialog.resizableProperty().setValue(false);

        exceptionImg = new ImageView("css/Close_40px.png");
        exceptionDialog.setGraphic(exceptionImg);

        exceptionDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

    }

    public void createExceptionDialog(Exception exception)
    {
        exceptionDialog.setHeaderText(exception.getMessage());
        exceptionDialog.show();
    }

    public Dialog<Exception> getExceptionDialog() {
        return exceptionDialog;
    }
}
