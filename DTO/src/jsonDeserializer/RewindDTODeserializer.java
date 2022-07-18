package jsonDeserializer;
import DTO.CustomerDTO;
import DTO.LoanDTO;
import DTO.RewindDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RewindDTODeserializer implements JsonDeserializer<RewindDTO> {

    @Override
    public RewindDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        //deserialize customer dtos
        Type customersDTOsType = new TypeToken<List<CustomerDTO>>() {
        }.getType();
        JsonArray customerDTOsAsJsonArray = jsonObject.getAsJsonArray("customers");
        List<CustomerDTO> customerDTOSInList = GsonWrapper.GSON.fromJson(customerDTOsAsJsonArray, customersDTOsType);
        ArrayList<CustomerDTO> customersDTOS = new ArrayList<>(customerDTOSInList);

        //deserialize loan dtos
        Type loanDTOsType = new TypeToken<ArrayList<LoanDTO>>() {
        }.getType();
        JsonArray loansDTOSAsJsonArray = jsonObject.getAsJsonArray("loans");
        List<LoanDTO> loansDTOSInList = GsonWrapper.GSON.fromJson(loansDTOSAsJsonArray, loanDTOsType);
        ArrayList<LoanDTO> loansDTOS = new ArrayList<>(loansDTOSInList);

        int currentYaz = jsonObject.get("currentYaz").getAsInt();

        RewindDTO rewindDTO = new RewindDTO(currentYaz, customersDTOS, loansDTOS);

        return rewindDTO;
    }
}
