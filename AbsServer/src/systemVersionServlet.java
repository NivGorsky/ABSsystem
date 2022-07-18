import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonDeserializer.GsonWrapper;

import java.io.IOException;

@WebServlet("/version")
public class systemVersionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");

        String consumer = request.getParameter("consumer");

        switch (consumer) {
            case "CUSTOMER": {
                int custVersion = ServletUtils.getCustomerVersion();
                response.getWriter().println(GsonWrapper.GSON.toJson(custVersion));
                break;
            }
            case "ADMIN": {
                int adminVersion = ServletUtils.getAdminVersion();
                response.getWriter().println(GsonWrapper.GSON.toJson(adminVersion));
                break;
            }
        }
        response.getWriter().close();
    }
}
