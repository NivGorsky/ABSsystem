import Engine.MainSystem;
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
                //increase yaz
                engine.moveTimeLine();
                ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
                ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
                break;
            }
            case "-": {
                //decrease yaz
                ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
                ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
                break;
            }
            case "=": {
                break;
            }
        }

        response.getWriter().print(engine.getCurrYaz());
        response.getWriter().close();
    }
}