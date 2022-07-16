import DTO.NotificationsDTO;
import Engine.MainSystem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/notifications")
public class NotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        String customerName = request.getParameter("customer-name");
        MainSystem engine = ServletUtils.getAbsSystem(getServletContext());

        try{
            NotificationsDTO notificationsDTO = engine.getNotificationsDTO(customerName);
            String notificationsDTOAsJson = ServletUtils.GSON.toJson(notificationsDTO);
            response.getWriter().print(notificationsDTOAsJson);
        }

        catch (Exception e){
            String errorMessage = e.getMessage();
            response.setStatus(403);
            response.getWriter().println(errorMessage);
        }

        finally {
            response.getWriter().close();
        }
    }
}