package jsonDeserializer;

import Engine.ABSsystem;
import Engine.Account;
import Engine.Loan;
import Engine.LoanPaymentsData;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LoanDeserializer implements JsonDeserializer<Loan> {

    private final ABSsystem system;

    public LoanDeserializer(ABSsystem system) { this.system = system; }

    @Override
    public Loan deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Loan newLoan = new Loan();

        String loanName = jsonObject.getAsJsonObject("loanName").getAsString();
        String category = jsonObject.getAsJsonObject("category").getAsString();
        int initialAmount = jsonObject.getAsJsonObject("initialAmount").getAsInt();
        String borrowerName = jsonObject.getAsJsonObject("customerName").getAsString();
        double interestPerPaymentSetByBorrowerInPercents = jsonObject.getAsJsonObject("interestPerPayment").getAsDouble();
        double totalInterestForLoan = jsonObject.getAsJsonObject("totalInterest").getAsDouble();
        Loan.LoanStatus status = GsonWrapper.GSON.fromJson(jsonObject.getAsJsonObject("status"), Loan.LoanStatus.class);
        Type accountType = new TypeToken<Account>(){}.getType();
        JsonObject accountAsJson = jsonObject.getAsJsonObject("account");
        Account account = GsonWrapper.GSON.fromJson(accountAsJson, accountType);


                                //payments data
        Type loanPaymentsDataType = new TypeToken<LoanPaymentsData>(){}.getType();
        JsonObject paymentsDataAsJson = jsonObject.getAsJsonObject("paymentsData");
        LoanPaymentsData paymentsData = GsonWrapper.GSON.fromJson(paymentsDataAsJson, loanPaymentsDataType);
        int paymentRateInYaz = jsonObject.get("paymentRateInYaz").getAsInt();
        double interestPaid = jsonObject.get("interestPaid").getAsDouble();
        double amountPaid = jsonObject.get("amountPaid").getAsDouble();
        double debt = jsonObject.get("debt").getAsDouble();


                                    //time line data
        int totalYazToPay = jsonObject.getAsJsonObject("maxYazToPay").get("value").getAsInt();
        int activationYaz = jsonObject.getAsJsonObject("activationYaz").getAsInt();
        int yazRemainingToPay = jsonObject.getAsJsonObject("yazRemainingToPay").getAsInt();
        int finishYaz = jsonObject.getAsJsonObject("finish").getAsInt();


                                    //loan's lenders' data
        Type lendersArrayType = new TypeToken<ArrayList<Loan.LenderDetails>>(){}.getType();
        JsonArray lendersArrayAsJsonArray = jsonObject.getAsJsonArray("lendersBelongToLoan");
        List<Loan.LenderDetails> lendersBelongToLoanAsList = GsonWrapper.GSON.fromJson(lendersArrayAsJsonArray, lendersArrayType);
        LinkedList<Loan.LenderDetails> lendersBelongToLoan = new LinkedList<>(lendersBelongToLoanAsList);
        double loanPercentageTakenByLenders = jsonObject.get("loanPercentageTakenByLenders").getAsDouble();
        double loanAmountFinancedByLenders = jsonObject.get("loanAmountFinancedByLenders").getAsDouble();


        newLoan.setLoanFromJson(loanName, category, initialAmount, borrowerName, interestPerPaymentSetByBorrowerInPercents, totalInterestForLoan,
                status, account, paymentsData, paymentRateInYaz, interestPaid, amountPaid, debt, totalYazToPay, activationYaz, yazRemainingToPay, finishYaz,
                lendersBelongToLoan, loanPercentageTakenByLenders, loanAmountFinancedByLenders);

        return newLoan;
    }

}














