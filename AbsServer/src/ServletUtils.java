import Engine.ABSsystem;
import Engine.MainSystem;
import jakarta.servlet.ServletContext;

public class ServletUtils {

    private static final String MAIN_SYSTEM_ATTRIBUTE_NAME = "mainSystem";
    private static final Object mainSystemLock = new Object();

    public static MainSystem getAbsSystem(ServletContext servletContext) {

        synchronized (mainSystemLock) {
            if (servletContext.getAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME, new ABSsystem());
            }
        }
        return (MainSystem) servletContext.getAttribute(MAIN_SYSTEM_ATTRIBUTE_NAME);
    }

}
