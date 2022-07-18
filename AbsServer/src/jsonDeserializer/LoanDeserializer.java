package jsonDeserializer;

import DTO.LoanDTO;
import Engine.ABSsystem;
import Engine.Account;
import Engine.Loan;
import Engine.LoanPaymentsData;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class LoanDeserializer implements JsonDeserializer<Loan> {

    private final ABSsystem system;

    public LoanDeserializer(ABSsystem system) { this.system = system; }

    @Override
    public Loan deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String loanName = jsonObject.getAsJsonObject("loanName").get("value").getAsString();
        String borrowerName = jsonObject.getAsJsonObject("customerName").get("value").getAsString();
        int initialAmount = jsonObject.getAsJsonObject("initialAmount").get("value").getAsInt();
        int totalYazToPay = jsonObject.getAsJsonObject("maxYazToPay").get("value").getAsInt();
        double interestPerPaymentSetByBorrowerInPercents = jsonObject.getAsJsonObject("interestPerPayment").get("value").getAsDouble();
        double totalInterestForLoan = jsonObject.getAsJsonObject("totalInterest").get("value").getAsDouble();
        int paymentRateInYaz = jsonObject.getAsJsonObject("yazPerPayment").get("value").getAsInt();
        String status = jsonObject.getAsJsonObject("status").get("value").getAsString();
        String category = jsonObject.getAsJsonObject("category").get("value").getAsString();
        double interestPaid = jsonObject.getAsJsonObject("paidInterest").get("value").getAsDouble();
        double amountPaid = jsonObject.getAsJsonObject("paidLoan").get("value").getAsDouble();
        double debt = jsonObject.getAsJsonObject("debt").get("value").getAsDouble();
        int activationYaz = jsonObject.getAsJsonObject("activationYaz").get("value").getAsInt();
        int yazRemainingToPay = jsonObject.getAsJsonObject("yazRemainingToPay").get("value").getAsInt();
        int finishYaz = jsonObject.getAsJsonObject("finishYaz").get("value").getAsInt();
        double loanPercentageTakenByLenders = jsonObject.getAsJsonObject("loanPercentageTakenByLenders").get("value").getAsDouble();
        double loanAmountFinancedByLenders = jsonObject.getAsJsonObject("loanAmountFinancedByLenders").get("value").getAsDouble();


        Type accountType = new TypeToken<Account>(){}.getType();
        JsonObject accountAsJson = jsonObject.getAsJsonObject("account");
        Account account = GsonWrapper.GSON.fromJson(accountAsJson, accountType);

        Type loanPaymentsDataType = new TypeToken<LoanPaymentsData>(){}.getType();
        JsonObject paymentsDataAsJson = jsonObject.getAsJsonObject("paymentsData");
        LoanPaymentsData paymentsData = GsonWrapper.GSON.fromJson(accountAsJson, loanPaymentsDataType);

        Type lendersArrayType = new TypeToken<ArrayList<Loan.LenderDetails>>(){}.getType();
        JsonObject lendersArrayAsJson = jsonObject.getAsJsonObject("lendersBelongToLoan");
        ArrayList<Loan.LenderDetails> lendersBelongToLoan = GsonWrapper.GSON.fromJson(lendersArrayAsJson, lendersArrayType);

        Loan loan = new Loan()



    }

}
