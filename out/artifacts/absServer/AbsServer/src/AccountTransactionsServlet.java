import DTO.AccountMovementDTO;
import DTO.CustomerDTO;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String customerName = request.getParameter("customer-name");
        MainSystem mainSystem = ServletUtils.getAbsSystem(getServletContext());
        CustomerDTO customerDTO = mainSystem.getCustomerDTO(customerName);

        ArrayList<AccountMovementDTO> accountMovementDTOArrayList = mainSystem.getCustomerDTO(customerName).getAccountMovements();
        String accountTransactionsInfoAsJsonString = ServletUtils.GSON.toJson(accountMovementDTOArrayList);

        response.getWriter().print(accountTransactionsInfoAsJsonString);
        response.getWriter().flush();
    }
}

