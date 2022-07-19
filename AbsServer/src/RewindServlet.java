import Engine.ABSsystem;
import Engine.MainSystem;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/rewind")
public class RewindServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        String operation = request.getParameter("operation");

        switch (operation) {
            case "start": {
                String yazToRewindAsString = request.getParameter("yaz-rewind");
                int yazToRewind = Integer.parseInt(yazToRewindAsString);
                ServletUtils.setIsRewind(true);
                ServletUtils.setRewindYaz(yazToRewind);
                ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
                ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
//                System.out.println("customer version updated: "+ ServletUtils.getCustomerVersion());
                break;
            }

            case "end": {
                ServletUtils.setIsRewind(false);
                ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
                ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
                break;
            }
        }
    }
}