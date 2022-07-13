package customerScene.loanTrading;

import DTO.LoanDTO;
import DTO.LoanForSaleDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import loansTable.LoansTableComponentController;
import main.Configurations;
import mutualInterfaces.ParentController;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoanTradingSceneController {

    @FXML private ScrollPane loansTrade;
    @FXML private AnchorPane loansForSaleAP;
    @FXML private Button buyLoanButton;
    @FXML private AnchorPane loansAsLenderAP;
    @FXML private Button sellLoanButton;
    @FXML private Label loanPriceLabel;
    @FXML private Label sellerLabel;

    @FXML private ScrollPane loansTableComponent;
    @FXML private LoansTableComponentController loansTableComponentController;

    private LoanDTO selectedLoanFromLoansTable;
    private SimpleBooleanProperty isLoanSelected;
    private SimpleDoubleProperty loanPrice;
    private SimpleStringProperty seller;

    private ParentController parentController;

    public LoanTradingSceneController() {
        selectedLoanFromLoansTable = null;
        isLoanSelected = new SimpleBooleanProperty(false);
        loanPrice = new SimpleDoubleProperty();
        seller = new SimpleStringProperty();
    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }

    @FXML public void initialize() {
        loanPriceLabel.visibleProperty().bind(isLoanSelected);
        loanPriceLabel.textProperty().bind(Bindings.concat("Loan Price: " , loanPrice.getValue()));

        sellerLabel.visibleProperty().bind(isLoanSelected);
        sellerLabel.textProperty().bind(Bindings.concat("Seller: " , seller.getValue()));
    }

    @FXML
    void buyLoanButtonClicked(ActionEvent event) {
        sendHttpRequest("BUY");
    }

    @FXML
    void sellLoanButtonClicked(ActionEvent event) {
        sendHttpRequest("SELL");
    }

    private void sendHttpRequest(String action) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/LoanTrading").newBuilder();
        urlBuilder.addQueryParameter("Action", action);
        String finalUrl = urlBuilder.build().toString();
        Request request = new Request.Builder().url(finalUrl).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback tradeLoanCallBack = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                parentController.createExceptionDialog(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(Integer.toString(response.code()))));
                }

                else {
                    Platform.runLater(() -> {
                        loadLoansToTables(); //TODO!!!
                    });
                }
            }
        };

        call.enqueue(tradeLoanCallBack);
    }

    public void loadLoansToTables() {
        //TODO!!!!
    }

}
