import Engine.ABSsystem;
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
        MainSystem engine = ServletUtils.getAbsSystem(getServletContext());

        switch (moveDirection) {
            case "+": {
                synchronized (this) {
                    engine.moveTimeLine();
                    ServletUtils.setCurrentYaz(ServletUtils.getCurrentYaz() + 1);
                }
                ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
                ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
                break;
            }
//            case "-": {
//                String yazToRewindAsString = request.getParameter("yaz-rewind");
//                int yazToRewind = Integer.parseInt(yazToRewindAsString);
//                rewindOrStopRewind(yazToRewind);
//
//                if(ServletUtils.getIsRewind() == true){
//                    ServletUtils.setRewindYaz(yazToRewind);
//                }
//                ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
//                ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
//                System.out.println("customer version updated: "+ ServletUtils.getCustomerVersion());
//                break;
//            }
            case "=": {
                break;
            }
        }

        response.getWriter().print(engine.getCurrYaz());
        response.getWriter().close();
    }
}