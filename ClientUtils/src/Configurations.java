import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;


public class Configurations {
    public final static String BASE_URL = "http://localhost:8080";
    public final static OkHttpClient HTTP_CLIENT = new OkHttpClient();
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");

    };



}
