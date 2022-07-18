package jsonDeserializer;
import DTO.AccountMovementDTO;
import DTO.CustomerDTO;
import DTO.LoanDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javax.security.auth.login.Configuration;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

public class CustomerDTODeserializer implements JsonDeserializer<CustomerDTO> {

    @Override
    public CustomerDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String customerName = jsonObject.getAsJsonObject("customerName").get("value").getAsString();
        double balance = jsonObject.getAsJsonObject("balance").get("value").getAsDouble();

        CustomerDTO customerDTO = new CustomerDTO(customerName, balance);

        Type accountMovementsType = new TypeToken<List<AccountMovementDTO>>(){}.getType();
        JsonArray accountAsMovementsJsonArray = jsonObject.getAsJsonArray("accountMovements");
        List<AccountMovementDTO> accountMovementDTOSInList = GsonWrapper.GSON.fromJson(accountAsMovementsJsonArray, accountMovementsType);
        ArrayList<AccountMovementDTO> accountMovementDTOS = new ArrayList<>(accountMovementDTOSInList);

        Type loanDTOsType = new TypeToken<ArrayList<LoanDTO>>(){}.getType();
        JsonArray loansAsBorrowerAsJsonArray = jsonObject.getAsJsonArray("loansAsBorrower");
        List<LoanDTO> loansAsBorrowerInList = GsonWrapper.GSON.fromJson(loansAsBorrowerAsJsonArray, loanDTOsType);
        ArrayList<LoanDTO> loansAsBorrower = new ArrayList<>(loansAsBorrowerInList);

        JsonArray loansAsLenderAsJsonArray = jsonObject.getAsJsonArray("loansAsLender");
        List<LoanDTO> loansAsLenderInList = GsonWrapper.GSON.fromJson(loansAsLenderAsJsonArray, loanDTOsType);
        ArrayList<LoanDTO> loansAsLender = new ArrayList<>(loansAsLenderInList);

        customerDTO.setLoansAsBorrower(loansAsBorrower);
        customerDTO.setLoansAsBorrower(loansAsBorrower);

        return customerDTO;
    }




}
