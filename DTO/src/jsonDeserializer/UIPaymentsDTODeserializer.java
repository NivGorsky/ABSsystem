package jsonDeserializer;

import DTO.LoanDTO;
import DTO.UIPaymentDTO;
import com.google.gson.*;

import java.lang.reflect.Type;

public class UIPaymentsDTODeserializer implements JsonDeserializer<UIPaymentDTO> {

    @Override
    public UIPaymentDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        //extract raw data
        JsonObject jsonObject = json.getAsJsonObject();
        UIPaymentDTO uiPaymentDTO = new UIPaymentDTO();
        uiPaymentDTO.customerName = jsonObject.get("customerName").getAsString();
        uiPaymentDTO.operation = jsonObject.get("operation").getAsString();
        JsonObject loanDTOAsJsonObject = jsonObject.getAsJsonObject("loanDTO");
        uiPaymentDTO.loanDTO = GsonWrapper.GSON.fromJson(loanDTOAsJsonObject, LoanDTO.class);
        uiPaymentDTO.yaz = jsonObject.get("yaz").getAsInt();
        uiPaymentDTO.amount = jsonObject.get("amount").getAsDouble();

        if(jsonObject.has("lenderDetailsDTO")){
            JsonObject lenderDetailsDTOAsJsonObject = jsonObject.getAsJsonObject("lenderDetailsDTO");
            uiPaymentDTO.lenderDetailsDTO = GsonWrapper.GSON.fromJson(lenderDetailsDTOAsJsonObject, LoanDTO.LenderDetailsDTO.class);
        }

        return uiPaymentDTO;
    }



}

