import DTO.CustomerDTO;
import DTO.RewindDTO;
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
        String customerName = request.getParameter("customerName");

        MainSystem engine = ServletUtils.getAbsSystem(getServletContext());

        switch (consumer) {
            case "ADMIN": {
                RewindDTO res = engine.getAdminRewindData(ServletUtils.getCurrentYaz());
                response.getWriter().println(ServletUtils.GSON.toJson(res));
                break;
            }
            case "CUSTOMER": {
                CustomerDTO res = engine.getCustomerRewindData(ServletUtils.getCurrentYaz(), customerName);
                response.getWriter().println(ServletUtils.GSON.toJson(res));

                break;
            }
        }
        
        response.getWriter().close();
    }
}
