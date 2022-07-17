import Engine.MainSystem;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currentYaz")
public class CurrentYazServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        String moveDirection = request.getParameter("move-direction");
        MainSystem AbsSystem = ServletUtils.getAbsSystem(getServletContext());

        switch (moveDirection) {
            case "+": {
                synchronized (this) {
                    ServletUtils.saveCurrentSystem(getServletContext());
                    //increase yaz
                }
                ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
                ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
                break;
            }
            case "-": {
                int yazToRewind = Integer.parseInt(request.getParameter("yaz-rewind"));
                rewindOrStopRewind(yazToRewind);
                ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
                ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
                break;
            }
            case "=": {
                break;
            }
        }

        response.getWriter().print(AbsSystem.getCurrYaz());
        response.getWriter().close();
    }


    private void rewindOrStopRewind(int yazToRewind) {
        ServletContext servletContext = getServletContext();
        int lastYaz = ServletUtils.getLastYaz(servletContext);
        MainSystem system = null;

        if(yazToRewind == 0) {
            ServletUtils.setIsRewind(false);
            system = ServletUtils.getAbsSystemInSpecificYaz(servletContext,lastYaz);
            ServletUtils.setAbsSystem(servletContext, system);
        }
        else {
            ServletUtils.setIsRewind(true);
            system =  ServletUtils.getAbsSystemInSpecificYaz(servletContext, yazToRewind);
            ServletUtils.setAbsSystem(servletContext, system);
        }
    }
}