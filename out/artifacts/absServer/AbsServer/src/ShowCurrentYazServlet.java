import Engine.MainSystem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/current-yaz")
public class ShowCurrentYazServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/plain;charset=UTF-8");

        MainSystem AbsSystem = ServletUtils.getAbsSystem(getServletContext());
        response.getWriter().println("Current Yaz: " + AbsSystem.getCurrYaz());
    }
}
