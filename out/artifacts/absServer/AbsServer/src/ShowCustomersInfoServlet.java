import DTO.CustomerDTO;
import DTO.LoanDTO;
import Engine.MainSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ShowCustomersInfo", urlPatterns = "/showCustomersInfo")
public class ShowCustomersInfoServlet extends HttpServlet
{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        MainSystem mainSystem = ServletUtils.getAbsSystem(getServletContext());

        ArrayList<CustomerDTO> customersInfo = mainSystem.showCustomersInfo();
        String customersInfoAsJsonString = ServletUtils.GSON.toJson(customersInfo);
        response.getWriter().print(customersInfoAsJsonString);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
