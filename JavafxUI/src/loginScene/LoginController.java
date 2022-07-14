package loginScene;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import main.Configurations;
import mutualInterfaces.BaseController;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;


public class LoginController {

    public enum LoginType {
        ADMIN,
        CUSTOMER
    }

    @FXML private ScrollPane login;
    @FXML private Label loginLabel;
    @FXML private Label nameLabel;
    @FXML private TextField nameTextField;
    @FXML private Button logInButton;

    private ParentController parentController;
    private BaseController baseController;
    private LoginType loginType;

    public void setLoginType(LoginType type) { this.loginType = type; }

    @FXML
    public void loginButtonClicked(ActionEvent event) {
        if(nameTextField.getText().isEmpty()) {
            return;
        }
        else{
            tryLogIn();
        }

    }

    public LoginController(){

    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
        this.baseController = (BaseController)parentController;
    }

    private void tryLogIn() {
        String name = nameTextField.getText();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/login").newBuilder();
        urlBuilder.addQueryParameter("Login-type", loginType.toString());
        String finalUrl = urlBuilder.build().toString();

        String nameAsJson = Configurations.GSON.toJson(name);
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(nameAsJson.getBytes()))
                .build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback loginCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = Configurations.GSON.fromJson(response.body().string(), String.class);
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(Integer.toString(response.code())
                               + "\n" + responseBody)));
                }
                else {
                    Platform.runLater(() -> {
                        baseController.setLoggedInDetails(name);
                    });
                }
            }
        };

        call.enqueue(loginCallBack);
    }


}
