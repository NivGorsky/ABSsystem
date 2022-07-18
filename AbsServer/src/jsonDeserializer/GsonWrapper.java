package jsonDeserializer;

import DTO.LoanDTO;
import DTO.LoanPlacingDTO;
import DTO.UIPaymentDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonWrapper {
    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LoanPlacingDTO.class, new LoanPlacingDTODeserializer())
            .registerTypeAdapter(UIPaymentDTO.class, new UIPaymentsDTODeserializer())
            .registerTypeAdapter(LoanDTO.class, new LoanDtoDeserializer())
            .registerTypeAdapter(LoanDTO.LenderDetailsDTO.class, new LenderDetailsDTODeserializer())
            .create();
}
