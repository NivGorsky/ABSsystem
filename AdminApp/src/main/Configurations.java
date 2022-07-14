package main;

import DTO.LoanDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jsonDeserializer.LoanDtoDeserializer;
import okhttp3.OkHttpClient;


public class Configurations {
    public final static String BASE_URL = "http://localhost:8080/AbsServer";
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static Gson GSON = new GsonBuilder().registerTypeAdapter(LoanDTO.class, new LoanDtoDeserializer()).create();
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static int REFRESH_RATE = 2;
}
