package adminBase;

import adminScene.AdminSceneController;
import exceptionDialog.ExceptionDialogCreator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loginScene.LoginController;
import main.Configurations;
import mutualInterfaces.BaseController;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class AdminBaseController implements ParentController, BaseController {

    @FXML private ScrollPane root;
    @FXML private BorderPane adminScene;
    @FXML private AdminSceneController adminSceneController;
    @FXML private ScrollPane loginScene;
    @FXML private LoginController loginSceneController;

    private BooleanProperty isLoggedIn;
    private Stage primaryStage;

    public AdminBaseController(){
        isLoggedIn = new SimpleBooleanProperty();
        isLoggedIn.addListener(((observable, oldValue, newValue) -> {

            if(newValue == true){
                root.setContent(adminScene);
                adminSceneController.onShow();
            }

            else{
                root.setContent(loginScene);
            }
        }));
    }

    @FXML public void initialize() {
        loadAdminScene();
        if(loginSceneController != null && adminSceneController != null)
        {
            adminSceneController.setParentController(this);
            loginSceneController.setParentController(this);
            loginSceneController.setLoginType(LoginController.LoginType.ADMIN);
        }
    }

    public void adminLoggedOut() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/logout").newBuilder();
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder().url(finalUrl).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback logoutCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ExceptionDialogCreator.createExceptionDialog(e);                    }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                response.close();
            }
        };

        call.enqueue(logoutCallBack);
    }

    @Override
    public void createExceptionDialog(Exception ex) {
        ExceptionDialogCreator.createExceptionDialog(ex);
    }

    @Override
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void switchStyleSheet(String selectedItem) {
        switch (selectedItem)
        {
            case("Light Mode"):
            {
                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(
                        getClass().getResource("/css/LightMode.css").toExternalForm());

                break;
            }
            case ("Dark Mode"):
            {
                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(
                        getClass().getResource("/css/DarkMode.css").toExternalForm());

                break;
            }
            case ("MTA Mode"):
            {

                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(
                        getClass().getResource("/css/MTAMode.css").toExternalForm());

                break;
            }
            case ("Barbi Mode"):
            {
                primaryStage.getScene().getStylesheets().clear();
                primaryStage.getScene().getStylesheets().add(
                        getClass().getResource("/css/BarbiMode.css").toExternalForm());

                break;
            }
        }
    }

    @Override
    public String getLoggedInUser() {
        return adminSceneController.getAdminName();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setRoot(ScrollPane root){
        this.root = root;
    }

    @Override
    public void setLoggedInDetails(String name){
       adminSceneController.setAdmin(name);
        isLoggedIn.set(true);
    }

    private void loadAdminScene() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("/adminScene/adminScene.fxml");
            loader.setLocation(mainFXML);
            adminScene = loader.load();
            adminSceneController = loader.getController();

        } catch (Exception e) {
            createExceptionDialog(e);
        }
    }
}
