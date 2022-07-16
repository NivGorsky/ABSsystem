package jsonDeserializer;

import DTO.LoanCategoriesDTO;
import DTO.LoanDTO;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoanCategoriesDeserializer implements JsonDeserializer<LoanCategoriesDTO> {

    Gson gson = new Gson();

    @Override
    public LoanCategoriesDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        Type categoriesDtoType = new TypeToken<List<String>>(){}.getType();
        JsonArray categoriesAsJsonObject =  jsonObject.getAsJsonArray("loanCategories");
        List<String> loanCategories = gson.fromJson(categoriesAsJsonObject, categoriesDtoType);

        ArrayList<String> categories = new ArrayList<>(loanCategories);
        LoanCategoriesDTO loanCategoriesDTO = new LoanCategoriesDTO(categories);

        return loanCategoriesDTO;
    }
}
