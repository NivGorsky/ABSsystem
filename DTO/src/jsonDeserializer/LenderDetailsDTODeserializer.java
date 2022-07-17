package jsonDeserializer;

import DTO.LoanDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class LenderDetailsDTODeserializer implements JsonDeserializer<LoanDTO.LenderDetailsDTO> {

    @Override
    public LoanDTO.LenderDetailsDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        //extract raw data
        JsonObject jsonObject = json.getAsJsonObject();
        String lenderName = jsonObject.getAsJsonObject("lenderName").get("value").getAsString();
        Double lendersInvestAmount = jsonObject.getAsJsonObject("lendersInvestAmount").get("value").getAsDouble();

        return new LoanDTO.LenderDetailsDTO(lenderName, lendersInvestAmount);
    }
}
