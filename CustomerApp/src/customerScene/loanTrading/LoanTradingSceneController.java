package customerScene.loanTrading;

import DTO.LoanDTO;
import DTO.LoanForSaleDTO;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private SimpleBooleanProperty isLoanSelected;
    Map<String, LoanDTO> seller2LoansForSale;

    private ParentController parentController;

    public LoanTradingSceneController() {
        isLoanSelected = new SimpleBooleanProperty(false);
        seller2LoansForSale = new HashMap<>();
    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }

    @FXML public void initialize() {

        loanPriceLabel.visibleProperty().bind(isLoanSelected);
        loanPriceLabel.textProperty().bind(Bindings.concat("Loan Price: " , calcLoanPrice()));

        sellerLabel.visibleProperty().bind(isLoanSelected);
        sellerLabel.textProperty().bind(Bindings.concat("Seller: " , findSeller()));
    }

    private double calcLoanPrice() {
        double price = 0;
        String seller = findSeller();

        for(LoanDTO.LenderDetailsDTO lenderDetails : loansTableComponentController.getSelectedLoanFromTable().getLenderDTOS()) {
            if(lenderDetails.getLenderName() == seller) {
                price = lenderDetails.getLendersInvestAmount();
            }
        }

        return price;
    }

    private String findSeller() {
        String seller = null;
        for(Map.Entry<String, LoanDTO> item : seller2LoansForSale.entrySet()) {
            if(item.getValue() == loansTableComponentController.getSelectedLoanFromTable()) {
                seller = item.getKey();
                break;
            }
        }
        return seller;
    }

    @FXML
    void buyLoanButtonClicked(ActionEvent event) {
        LoanDTO selectedLoan = loansTableComponentController.getSelectedLoanFromTable();
        sendTradeHttpRequest("BUY", findSeller(), selectedLoan.getLoanName());
    }

    @FXML
    void sellLoanButtonClicked(ActionEvent event) {
        LoanDTO selectedLoan = loansTableComponentController.getSelectedLoanFromTable();
        sendTradeHttpRequest("SELL", parentController.getLoggedInUser(), selectedLoan.getLoanName());
    }

    private void sendTradeHttpRequest(String action, String sellerName, String loanName) {
        LoanForSaleDTO loanForSale = null;
        if(action == "SELL") {
            loanForSale = new LoanForSaleDTO(null, parentController.getLoggedInUser(), loanName, calcLoanPrice());
        }
        else {
            loanForSale = new LoanForSaleDTO(parentController.getLoggedInUser(), sellerName, loanName, calcLoanPrice());
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/LoanTrading").newBuilder();
        urlBuilder.addQueryParameter("Action", action);
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(Configurations.GSON.toJson(loanForSale).getBytes()))
                .build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback tradeLoanCallBack = new Callback() {
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
                        loadLoansToTables(); //TODO!!!
                    });
                }
            }
        };

        call.enqueue(tradeLoanCallBack);
    }

    private void requestSellersAndLoansForSale() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Configurations.BASE_URL + "/sellersAndLoansForSale").newBuilder();
        String finalUrl = urlBuilder.build().toString();

        Request request = new Request.Builder().url(finalUrl).build();

        Call call = Configurations.HTTP_CLIENT.newCall(request);
        Callback requestInfoCallBack = new Callback() {
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

        call.enqueue(requestInfoCallBack);
    }

    public void loadLoansToTables() {
        //TODO!!!!
    }

    public void onShow() {
        //getLoansForSaleMap and save it
        loadLoansToTables();
    }

}
