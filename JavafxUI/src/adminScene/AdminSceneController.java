package adminScene;

import generalScenes.HeaderPaneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

public class AdminSceneController {

    @FXML private Button increaseYazButton;
    @FXML private Button loadFileButton;


    @FXML public void increaseYazButtonClicked()
    {

    }

    @FXML public void loadFileButtonClicked()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");

        File selectedFile = fileChooser.showOpenDialog(primaryStage)
        

    }
}
