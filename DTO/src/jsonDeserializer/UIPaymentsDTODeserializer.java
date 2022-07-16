package jsonDeserializer;

import DTO.LoanDTO;
import DTO.UIPaymentDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javax.security.auth.login.Configuration;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

public class UIPaymentsDTODeserializer implements JsonDeserializer<UIPaymentDTO> {

    private Gson gson = new Gson();

    @Override
    public UIPaymentDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        //extract raw data
        JsonObject jsonObject = json.getAsJsonObject();
        UIPaymentDTO uiPaymentDTO = new UIPaymentDTO();
        uiPaymentDTO.customerName = jsonObject.get("customerName").getAsString();
        uiPaymentDTO.operation = jsonObject.get("operation").getAsString();
        JsonObject loanDTOAsJsonObject = jsonObject.getAsJsonObject("loanDTO");
        uiPaymentDTO.loanDTO = gson.fromJson(loanDTOAsJsonObject, LoanDTO.class);
        uiPaymentDTO.yaz = jsonObject.get("yaz").getAsInt();
        uiPaymentDTO.amount = jsonObject.get("amount").getAsDouble();

        String lenderName = jsonObject.getAsJsonObject("lenderDetailsDTO").get("lenderName").getAsString();
        Double amount = jsonObject.getAsJsonObject("lenderDetailsDTO").get("amount").getAsDouble();
        uiPaymentDTO.lenderDetailsDTO = new LoanDTO.LenderDetailsDTO(lenderName, amount);

        return uiPaymentDTO;
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

