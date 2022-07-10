package main;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;


public class Configurations {
    public final static String BASE_URL = "http://localhost:8080/AbsServer";
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static Gson GSON = new Gson();
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
}
