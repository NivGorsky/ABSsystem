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
        response.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
            response.setContentType("text/plain;charset=UTF-8");
            MainSystem engine = ServletUtils.getAbsSystem(getServletContext());
            String operation = request.getParameter("operation");
            String customerName = request.getParameter("customer-name");
            String amountAsString = request.getParameter("amount");

            handleOperation(operation, customerName, amountAsString, engine);
        }

        catch (Exception e){
            String errorMessage = e.getMessage();
            response.setStatus(403);
            response.getWriter().println(errorMessage);
        }
    }

    private void handleOperation(String operation, String customerName, String amountAsString, MainSystem engine) throws Exception {
        double amount = Double.parseDouble(amountAsString);

        switch (operation){
            case "withdraw":
                synchronized (this){
                    engine.withdrawMoney(customerName, amount);
                }

                break;

            case "deposit":
                synchronized (this){
                    engine.depositMoney(customerName, amount);
                }

                break;
        }
    }
}

