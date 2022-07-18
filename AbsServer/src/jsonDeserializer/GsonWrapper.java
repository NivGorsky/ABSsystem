package jsonDeserializer;

import Engine.ABSsystem;
import Engine.Customer;
import Engine.Loan;
import Engine.LoanPaymentsData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonWrapper {

    public static ABSsystem currentSystem;
    public static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Customer.class, new CustomerDeserializer(currentSystem))
            .registerTypeAdapter(Loan.class, new LoanDeserializer(currentSystem))
            .registerTypeAdapter(LoanPaymentsData.class, new LoanPaymentsDataDeserializer())
            .create();
}
