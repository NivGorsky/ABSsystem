package jsonDeserializer;
import DTO.LoanPlacingDTO;
import DTO.NotificationsDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoanPlacingDTODeserializer implements JsonDeserializer<LoanPlacingDTO> {

    private Gson gson = new Gson();

    @Override
    public LoanPlacingDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //extract raw data
        JsonObject jsonObject = json.getAsJsonObject();
        String customerName = jsonObject.get("customerName").getAsString();
        double amountToInvest = jsonObject.get("amountToInvest").getAsDouble();
        double minimumInterestPerYaz = jsonObject.get("minimumInterestPerYaz").getAsDouble();
        int minimumYazForReturn = jsonObject.get("minimumYazForReturn").getAsInt();
        int maximumPercentOwnership = jsonObject.get("maximumPercentOwnership").getAsInt();
        int maximumOpenLoansForBorrower = jsonObject.get("maximumOpenLoansForBorrower").getAsInt();

        Type categoriesWillingToInvestInType = new TypeToken<List<String>>(){}.getType();
        JsonArray categoriesWillingToInvestIn = jsonObject.getAsJsonArray("categoriesWillingToInvestIn");
        List<String> categoriesWillingToInvestInAsList = gson.fromJson(categoriesWillingToInvestIn, categoriesWillingToInvestInType);
        ArrayList<String> categoriesWillingToInvestInAsArray = new ArrayList<>(categoriesWillingToInvestInAsList);

        return new LoanPlacingDTO(amountToInvest, categoriesWillingToInvestInAsArray, minimumInterestPerYaz, minimumYazForReturn, maximumPercentOwnership, maximumOpenLoansForBorrower, customerName);
    }
}
