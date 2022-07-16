package customerScene.loanTrading;

import DTO.LoanDTO;
import DTO.LoanForSaleDTO;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoanTradingSceneController {

    @FXML private ScrollPane loanTradingScene;
    @FXML private AnchorPane loansForSaleAP;
    @FXML private Button buyLoanButton;
    @FXML private AnchorPane loansAsLenderAP;
    @FXML private Button sellLoanButton;
    @FXML private Label loanPriceLabel;
    @FXML private Label sellerLabel;

    @FXML private ScrollPane loansForSaleTable;
    @FXML private LoansTableComponentController loansForSaleTableController;

    @FXML private ScrollPane myLoansToSaleTable;
    @FXML private LoansTableComponentController myLoansToSaleTableController;

    private SimpleBooleanProperty isLoanSelected;
    Map<String, LoanDTO> seller2LoansForSale;
    private SimpleDoubleProperty loanPrice;
    private SimpleStringProperty loanSeller;

    private ParentController parentController;

    public LoanTradingSceneController() {
        isLoanSelected = new SimpleBooleanProperty(false);
        loanPrice = new SimpleDoubleProperty();
        loanSeller = new SimpleStringProperty();
        seller2LoansForSale = new HashMap<>();
    }

    public void setParentController(ParentController parentController) {
        this.parentController = parentController;
    }

    @FXML public void initialize() {
        loanPriceLabel.visibleProperty().bind(isLoanSelected);
        loanPriceLabel.textProperty().bind(Bindings.concat("Loan Price: " + loanPrice.get()));

        sellerLabel.visibleProperty().bind(isLoanSelected);
        sellerLabel.textProperty().bind(Bindings.concat("Seller: " + loanSeller.get()));

        loansForSaleTableController.setLoanSelectionListener((observable, oldValue, newValue) -> {
           findSeller();
           calcLoanPrice();
        });
    }

    private void calcLoanPrice() {
        double price = 0;

        for(LoanDTO.LenderDetailsDTO lenderDetails : loansForSaleTableController.getSelectedLoanFromTable().getLenderDTOS()) {
            if(lenderDetails.getLenderName() == loanSeller.get()) {
                price = lenderDetails.getLendersInvestAmount();
            }
        }

        loanPrice.set(price);
    }

    private void findSeller() {
        String seller = null;
        for(Map.Entry<String, LoanDTO> item : seller2LoansForSale.entrySet()) {
            if(item.getValue() == loansForSaleTableController.getSelectedLoanFromTable()) {
                seller = item.getKey();
                break;
            }
        }

        loanSeller.set(seller);
    }

    @FXML
    void buyLoanButtonClicked(ActionEvent event) {
        LoanDTO selectedLoan = loansForSaleTableController.getSelectedLoanFromTable();
        sendTradeHttpRequest("BUY", loanSeller.get(), selectedLoan.getLoanName());
    }

    @FXML
    void sellLoanButtonClicked(ActionEvent event) {
        LoanDTO selectedLoan = myLoansToSaleTableController.getSelectedLoanFromTable();
        sendTradeHttpRequest("SELL", parentController.getLoggedInUser(), selectedLoan.getLoanName());
    }

    private void sendTradeHttpRequest(String action, String sellerName, String loanName) {
        LoanForSaleDTO loanForSale = null;
        if(action == "SELL") {
            loanForSale = new LoanForSaleDTO(null, parentController.getLoggedInUser(), loanName, loanPrice.get());
        }
        else {
            loanForSale = new LoanForSaleDTO(parentController.getLoggedInUser(), sellerName, loanName, loanPrice.get());
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

                response.close();
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
                int responseCode = response.code();
                String body = response.body().string();
                response.close();
                if (responseCode != 200) {
                    String responseBody = Configurations.GSON.fromJson(body, String.class);
                    Platform.runLater(() ->
                            parentController.createExceptionDialog(new Exception(Integer.toString(responseCode)
                                    + "\n" + responseBody)));
                }

                else {
                    Platform.runLater(() -> {
                        Type typeOfMap = new TypeToken<Map<String, LoanDTO>>() {}.getType();
                        seller2LoansForSale = Configurations.GSON.fromJson(body, typeOfMap);
                    });
                }
            }
        };

        call.enqueue(requestInfoCallBack);
    }

    public void loadLoansToTables() {
        myLoansToSaleTableController.loadSpecificCustomerLoansAsLender(parentController.getLoggedInUser());
        if(seller2LoansForSale != null) {
            ArrayList<LoanDTO> loansForSale = new ArrayList<>(seller2LoansForSale.values());
            loansForSaleTableController.putLoansInTable(loansForSale);
        }
    }

    public void onShow() {
        requestSellersAndLoansForSale();
        loadLoansToTables();
    }


}
