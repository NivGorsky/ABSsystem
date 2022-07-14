package jsonDeserializer;

import DTO.LoanDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoanCategoriesDeserializer implements JsonDeserializer<LoanDTO> {

    Gson gson = new Gson();

    @Override
    public LoanDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Type loanDtoType = new TypeToken<ArrayList<String>>(){}.getType();
        JsonElement categoriesAsJson = 
    }
}
