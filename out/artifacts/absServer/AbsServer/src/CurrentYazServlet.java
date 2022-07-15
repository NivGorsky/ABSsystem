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
        MainSystem AbsSystem = ServletUtils.getAbsSystem(getServletContext());

        switch (moveDirection) {
            case "+": {
                //increase yaz
                break;
            }
            case "-": {
                //decrease yaz
                break;
            }
            case "=": {
                break;
            }
        }

        response.getWriter().print(AbsSystem.getCurrYaz());
        response.getWriter().close();
    }
}