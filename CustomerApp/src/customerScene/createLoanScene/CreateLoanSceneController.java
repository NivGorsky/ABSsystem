package customerScene.createLoanScene;

import DTO.LoanDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import customerBase.CustomerBaseController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import main.Configurations;
import mutualInterfaces.BaseController;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class CreateLoanSceneController {

    @FXML private ScrollPane createLoan;
    @FXML private TextField loanNameTF;
    @FXML private TextField amountTF;
    @FXML private TextField totalYazToPayTF;
    @FXML private TextField interestPerPaymentTF;
    @FXML private TextField paymentsRateTF;
    @FXML private ComboBox<String> categoriesCB;
    @FXML private Button createLoanButton;

    ParentController parentController;

    @FXML public void initialize() {
        createLoanButton.disableProperty().bind(loanNameTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(amountTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(totalYazToPayTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(interestPerPaymentTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(paymentsRateTF.textProperty().isEmpty());
        createLoanButton.disableProperty().bind(categoriesCB.selectionModelProperty().isNull());
    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void createLoanButtonClicked() {
        String custName = parentController.getLoggedInUser();
        double totalInterestForLoan = Integer.parseInt(interestPerPaymentTF.getText())*Double.parseDouble(amountTF.getText())/100;

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/createLoan").newBuilder();
        String finalUrl = urlBuilder.build().toString();

        LoanDTO newLoan = new LoanDTO(loanNameTF.getText(), custName, Integer.parseInt(amountTF.getText()), Integer.parseInt(totalYazToPayTF.getText()),
                Integer.parseInt(interestPerPaymentTF.getText()), totalInterestForLoan, Integer.parseInt(paymentsRateTF.getText()), "NEW",
                categoriesCB.getSelectionModel().getSelectedItem(), 0, 0, 0, 0);

        Request request = new Request.Builder().url(finalUrl).post(RequestBody.create(
                Configurations.GSON.toJson(newLoan).getBytes())).build();
        Call call = Configurations.HTTP_CLIENT.newCall(request);

        Callback newLoanCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(responseBody)));
                }
                else {
                    Platform.runLater(() -> {
                        //TODO: loansTableOnShow
                    });
                }
            }
        };

        call.enqueue(newLoanCallBack);
    }

    public void onShow() {
        getCategories();
    }

    private void getCategories() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/categories").newBuilder();
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder().url(finalUrl).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback categoriesCallBack = new Callback() {
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
                        Type typeOfHashMap = new TypeToken<Map<String, LoanDTO>>() {}.getType();
                        seller2LoansForSale = Configurations.GSON.fromJson(response.body().toString(), typeOfHashMap);
                    });
                }
            }
        };

        call.enqueue(categoriesCallBack);
    }
    }
}
