package jsonDeserializer;

import DTO.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class GsonWrapper {
    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LoanPlacingDTO.class, new LoanPlacingDTODeserializer())
            .registerTypeAdapter(UIPaymentDTO.class, new UIPaymentsDTODeserializer())
            .registerTypeAdapter(LoanDTO.class, new LoanDtoDeserializer())
            .registerTypeAdapter(LoanDTO.LenderDetailsDTO.class, new LenderDetailsDTODeserializer())
            .registerTypeAdapter(CustomerDTO.class, new CustomerDTODeserializer())
            .registerTypeAdapter(RewindDTO.class, new RewindDTODeserializer())
            .create();
}
