import DTO.CustomerDTO;
import DTO.LoanDTO;
import Engine.MainSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.security.auth.login.Configuration;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ShowLoansInfo", urlPatterns = "/showLoansInfo")
public class ShowLoansInfoServlet extends HttpServlet
{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        MainSystem mainSystem = ServletUtils.getAbsSystem(getServletContext());
        ArrayList<LoanDTO> loansInfo = new ArrayList<>();

        if(!request.getParameterMap().isEmpty()){
            String customerName = request.getParameter("customer-name");

            switch (request.getParameter("loan-type")){
                case "lender":
                    loansInfo = mainSystem.getLoansByCustomerNameAsLender(customerName);

                    break;

                case "borrower":
                    loansInfo = mainSystem.getLoansByCustomerNameAsBorrower(customerName);

                    break;
            }
        }

        else{
            loansInfo = mainSystem.showLoansInfo();
        }

        String loansInfoAsJsonString = ServletUtils.GSON.toJson(loansInfo);
        response.getWriter().print(loansInfoAsJsonString);
        response.getWriter().flush();
    }
}
