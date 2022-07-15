import DTO.LoanDTO;
import Engine.ABSsystem;
import Engine.MainSystem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletContext;
import jsonDeserializer.LoanDtoDeserializer;

public class ServletUtils {

    public static final String MAIN_SYSTEM_ATTRIBUTE_NAME = "mainSystem";
    private static final Object mainSystemLock = new Object();
    public final static Gson GSON = new GsonBuilder().registerTypeAdapter(LoanDTO.class, new LoanDtoDeserializer()).create();

    public static MainSystem getAbsSystem(ServletContext servletContext) {

        synchronized(mainSystemLock) {
            if (servletContext.getAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME, new ABSsystem());
            }
        }
        return (MainSystem) servletContext.getAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME);
    }

    public static Gson getGson(){
        return GSON;
    }
}
