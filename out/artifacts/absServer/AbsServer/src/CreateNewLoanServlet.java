import DTO.LoanDTO;
import Engine.MainSystem;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonDeserializer.GsonWrapper;

import javax.security.auth.login.Configuration;
import java.io.IOException;

@WebServlet("/createLoan")
public class CreateNewLoanServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        MainSystem mainSystem = ServletUtils.getAbsSystem(getServletContext());

        LoanDTO newLoan = GsonWrapper.GSON.fromJson(request.getReader(), LoanDTO.class);
        mainSystem.createNewLoan(newLoan);
        response.getWriter().close();

        ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
        ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
    }
}
