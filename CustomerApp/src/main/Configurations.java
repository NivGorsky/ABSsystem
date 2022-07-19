package main;
import DTO.LoanCategoriesDTO;
import DTO.LoanDTO;
import DTO.NotificationsDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jsonDeserializer.LoanCategoriesDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jsonDeserializer.NotificationsDTODeserializer;
import okhttp3.OkHttpClient;
import jsonDeserializer.LoanDtoDeserializer;

import java.io.File;
import java.io.FileWriter;

public class Configurations {
    public final static String BASE_URL = "http://localhost:8080/AbsServer";
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LoanDTO.class, new LoanDtoDeserializer())
            .registerTypeAdapter(LoanCategoriesDTO.class, new LoanCategoriesDeserializer())
            .registerTypeAdapter(NotificationsDTO.class, new NotificationsDTODeserializer())
            .create();

    public static FileWriter myWriter = null;
    public static void closeStream(){try{myWriter.close();} catch (Exception e){};}

    public final static void printToFile(String objectToPrint){

        if(myWriter == null){
            try{
                myWriter = new FileWriter("/Users/nivos/projects/ABSsystem/logFileCustomer.txt");
            }

            catch (Exception e){

            }
        }

        try{
            myWriter.write(GSON.toJson(objectToPrint));
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}


