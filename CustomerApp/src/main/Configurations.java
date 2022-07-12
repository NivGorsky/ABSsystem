package main;
import DTO.LoanDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import jsonDeserializer.LoanDtoDeserializer;

public class Configurations {
    public final static String BASE_URL = "http://localhost:8080/AbsServer";
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static Gson GSON = new GsonBuilder().registerTypeAdapter(LoanDTO.class, new LoanDtoDeserializer()).create();
}
