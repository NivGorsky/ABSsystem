package jsonDeserializer;
import DTO.LoanDTO;
import Engine.ABSsystem;
import Engine.Account;
import Engine.Customer;
import Engine.Loan;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CustomerDeserializer implements JsonDeserializer<Customer> {

    public ABSsystem currentSystemInstance;

    public CustomerDeserializer(ABSsystem i_currentSystemInstance){
        currentSystemInstance = i_currentSystemInstance;
    }

    @Override
    public Customer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        //extract raw data
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        Type loansArrayType = new TypeToken<List<Loan>>(){}.getType();
        JsonArray loansAsBorrowerAsJsonArray = jsonObject.getAsJsonArray("loansAsBorrower");
        JsonArray loansAsLenderAsJsonArray = jsonObject.getAsJsonArray("loansAsLender");
        List<Loan> loansAsBorrower = GsonWrapper.GSON.fromJson(loansAsBorrowerAsJsonArray, loansArrayType);
        List<Loan> loansAsLender = GsonWrapper.GSON.fromJson(loansAsLenderAsJsonArray, loansArrayType);
        Account account = GsonWrapper.GSON.fromJson(jsonObject.getAsJsonObject("account"), Account.class);

//        removeExistLoans(loansAsBorrower);
//        removeExistLoans(loansAsLender);
//        addLoansToMainSystemCurrentInstance(loansAsBorrower);
//        addLoansToMainSystemCurrentInstance(loansAsLender);

        Customer newCustomer = new Customer(name, -1);
        newCustomer.setAccount(account);
        addLoansToNewCustomer(newCustomer, loansAsBorrower, "borrower");
        addLoansToNewCustomer(newCustomer, loansAsLender, "lender");

        return newCustomer;
    }

    private void addLoansToNewCustomer(Customer newCustomer, List<Loan> loansToAdd, String type){
        Loan existLoanInSystem;

        for (Loan loan:loansToAdd) {
            existLoanInSystem = getExistLoanInSystem(loan);
            
            switch (type){
                case "borrower":
                    newCustomer.addLoanAsBorrower(existLoanInSystem);
                    break;

                case "lender":
                    newCustomer.addLoanAsLender(existLoanInSystem);
                    break;
            }
        }
    }

    private Loan getExistLoanInSystem(Loan loan){
        LinkedList<Loan> allLoansInSystem = currentSystemInstance.getAllLoans();

        for (Loan loanInSystem:allLoansInSystem) {
            if(loanInSystem.getLoanName().equals(loan.getLoanName())){
                return loanInSystem;
            }
        }

        throw new JsonParseException("could not find concrruent loan in CustomerDeserializer");
    }

    private void addLoansToMainSystemCurrentInstance(List<Loan> loans){
        for (Loan loanToAdd:loans) {
            currentSystemInstance.getAllLoans().add(loanToAdd);
        }
    }

    private void removeExistLoans(List<Loan> loansToCheck){
        LinkedList<Loan> AllLoansInSystem = currentSystemInstance.getAllLoans();
        int currentIndex = 0;

        for (Loan currentLoanToCheckIfExist:loansToCheck) {
            for (Loan loanInSystem: AllLoansInSystem){
                if(currentLoanToCheckIfExist.getLoanName().equals(loanInSystem.getLoanName())){
                    loansToCheck.remove(currentIndex);
                }
            }

            currentIndex+=1;
        }
    }
}
