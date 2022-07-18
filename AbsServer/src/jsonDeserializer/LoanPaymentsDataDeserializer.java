package jsonDeserializer;

import DTO.LoanDTO;
import Engine.ABSsystem;
import Engine.Account;
import Engine.Loan;
import Engine.LoanPaymentsData;
import Engine.PaymentsDB.PaymentsDB;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class LoanPaymentsDataDeserializer implements JsonDeserializer<LoanPaymentsData> {

    @Override
    public LoanPaymentsData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Type paymentsDataBasesType = new TypeToken<Map<LoanPaymentsData.PaymentType, PaymentsDB>>(){}.getType();
        JsonElement paymentsDataBasesAsJsonElement = jsonObject.get("paymentsDataBases");
        Map<LoanPaymentsData.PaymentType, PaymentsDB> paymentsDataBases = GsonWrapper.GSON.fromJson(paymentsDataBasesAsJsonElement, paymentsDataBasesType);
        LoanPaymentsData loanPaymentsData = new LoanPaymentsData();
        loanPaymentsData.setLoanPaymentsData(paymentsDataBases);

        return loanPaymentsData;
    }

}
