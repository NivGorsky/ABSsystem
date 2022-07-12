package jsonDeserializer;
import DTO.LoanDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.SortedMap;

public class LoanDtoDeserializer implements JsonDeserializer<LoanDTO> {

    @Override
    public LoanDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        //extract raw data
        JsonObject jsonObject = json.getAsJsonObject();
        String loanName = jsonObject.getAsJsonObject("loanName").get("value").getAsString();
        String customerName = jsonObject.getAsJsonObject("customerName").get("value").getAsString();
        double initialAmount = jsonObject.getAsJsonObject("initialAmount").get("value").getAsDouble();
        int totalYaz = jsonObject.getAsJsonObject("maxYazToPay").get("value").getAsInt();
        double interestPerPayment = jsonObject.getAsJsonObject("interestPerPayment").get("value").getAsDouble();
        double totalInterest = jsonObject.getAsJsonObject("totalInterest").get("value").getAsDouble();
        int yazPerPerPayment = jsonObject.getAsJsonObject("yazPerPayment").get("value").getAsInt();
        String status = jsonObject.getAsJsonObject("status").get("value").getAsString();
        String category = jsonObject.getAsJsonObject("category").get("value").getAsString();
        double paidInterest = jsonObject.getAsJsonObject("paidInterest").get("value").getAsDouble();
        double paidLoan = jsonObject.getAsJsonObject("paidLoan").get("value").getAsDouble();
        double debt = jsonObject.getAsJsonObject("debt").get("value").getAsDouble();
        double amountRaised = jsonObject.getAsJsonObject("amountRaised").get("value").getAsDouble();

        //build object manually
        LoanDTO loanDTO = new LoanDTO(loanName, customerName, initialAmount, totalYaz, interestPerPayment, totalInterest, yazPerPerPayment,
                status, category, paidInterest, paidLoan, debt, amountRaised);

        Type unpaidPaymentsType = new TypeToken<SortedMap<Integer, LoanDTO.PaymentDTO>>(){}.getType();
//        ArrayList<LoanDTO> loans = Configurations.GSON.fromJson(rawBody, arrayListLoanDtoType);

        //need to add the payments from the json to the loanDTO....
        



        return null;
    }

    private static String toPrettyJsonFormat(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(jsonString);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }


}
