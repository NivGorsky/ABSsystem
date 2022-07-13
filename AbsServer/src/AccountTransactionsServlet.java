import DTO.AccountMovementDTO;
import DTO.LoanDTO;
import Engine.MainSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/account-transactions")
public class AccountTransactionsServlet extends HttpServlet
{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        MainSystem mainSystem = ServletUtils.getAbsSystem(getServletContext());
        ArrayList<AccountMovementDTO> accountMovementDTOArrayList = mainSystem.getCustomerDTO().getAccountMovements()

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

}
