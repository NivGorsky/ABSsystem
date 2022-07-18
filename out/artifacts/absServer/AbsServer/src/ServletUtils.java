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
