package exceptionDialog;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ExceptionDialogCreator
{
    private static Stage primaryStage;
    private static ImageView exceptionImg;
    private static Dialog<Exception> exceptionDialog;

    public static void createExceptionDialog(Exception exception)
    {
        if(exceptionDialog == null)
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

        exceptionDialog.setHeaderText(exception.getMessage());
        exceptionDialog.show();
    }
}
