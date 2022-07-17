import DTO.*;
import Engine.ABSsystem;
import Engine.MainSystem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletContext;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import jsonDeserializer.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ServletUtils {

    public static final String MAIN_SYSTEM_ATTRIBUTE_NAME = "mainSystem";
    public static final String MAIN_SYSTEM_EACH_YAZ_ARRAY_NAME = "mainSystemAllYazArray";
    private static final Object mainSystemLock = new Object();
    private static final Object customerVersionLock = new Object();
    private static final Object adminVersionLock = new Object();
    private static final Object isRewindLock = new Object();
    public final static Gson GSON = new GsonBuilder().registerTypeAdapter(LoanPlacingDTO.class, new LoanDtoDeserializer()).create();

    private static int customerVersion = 1;
    private static int adminVersion = 1;
    private static boolean isRewind = false;

    //------------------------------METHODS------------------------------------------//

    public static MainSystem getAbsSystem(ServletContext servletContext) {

        synchronized(mainSystemLock) {
            if (servletContext.getAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME, new ABSsystem());
            }
        }
        return (MainSystem) servletContext.getAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME);
    }

    public static void setAbsSystem(ServletContext servletContext, MainSystem absSystem) {
        synchronized (mainSystemLock) {
            saveCurrentSystem(servletContext);
            servletContext.setAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME, absSystem);
        }
    }

    public static MainSystem getAbsSystemInSpecificYaz(ServletContext servletContext, int yaz) {
        synchronized (mainSystemLock) {
            if(servletContext.getAttribute(MAIN_SYSTEM_EACH_YAZ_ARRAY_NAME) == null) {
                servletContext.setAttribute(MAIN_SYSTEM_EACH_YAZ_ARRAY_NAME, new ArrayList<MainSystem>());
            }
        }

        ArrayList<MainSystem> array =(ArrayList<MainSystem>) (servletContext.getAttribute(MAIN_SYSTEM_EACH_YAZ_ARRAY_NAME));
        return array.get(yaz);
    }

    public static void saveCurrentSystem (ServletContext servletContext) {
            MainSystem currentAbsSystem = (MainSystem) servletContext.getAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME);
            ArrayList<MainSystem> array = (ArrayList<MainSystem>) (servletContext.getAttribute(MAIN_SYSTEM_EACH_YAZ_ARRAY_NAME));
            int currentYAZ = currentAbsSystem.getCurrYaz();
            array.add(currentYAZ - 1, currentAbsSystem);
    }

    public static int getLastYaz(ServletContext servletContext) {
        ArrayList<MainSystem> array =(ArrayList<MainSystem>) (servletContext.getAttribute(MAIN_SYSTEM_EACH_YAZ_ARRAY_NAME));
        return array.size();
    }

    public static void setIsRewind(boolean newValue) {
        synchronized (isRewindLock) {
            isRewind = newValue;
        }
    }

    public static boolean getIsRewind() { return isRewind; }

    public static Gson getGson(){
        return GSON;
    }

    public static int getCustomerVersion() {
            return customerVersion;
    }

    public static void setCustomerVersion(int customerVersion) {
        synchronized(customerVersionLock) {
            ServletUtils.customerVersion = customerVersion;
        }
    }

    public static int getAdminVersion() {
            return adminVersion;
    }

    public static void setAdminVersion(int adminVersion) {
        synchronized(adminVersionLock) {
            ServletUtils.adminVersion = adminVersion;
        }
    }

}
