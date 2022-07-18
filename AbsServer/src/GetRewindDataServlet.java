import Engine.MainSystem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/getRewindData")
public class GetRewindDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        String consumer = request.getParameter("consumer");
        MainSystem engine = ServletUtils.getAbsSystem(getServletContext());

        switch (consumer) {
            case "ADMIN": {
                
                break;
            }
            case "CUSTOMER": {

                break;
            }
        }

    }
}
