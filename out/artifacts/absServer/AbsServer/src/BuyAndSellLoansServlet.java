import DTO.LoanForSaleDTO;
import Engine.MainSystem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/LoanTrading")
public class BuyAndSellLoansServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        MainSystem AbsSystem = ServletUtils.getAbsSystem(getServletContext());
        String action = request.getParameter("Action");
        LoanForSaleDTO loan = ServletUtils.GSON.fromJson(request.getReader().lines()
                .collect(Collectors.joining()), LoanForSaleDTO.class);
        try {
            switch (action) {
                case "BUY": {
                    AbsSystem.buyLoan(loan);
                    ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
                    ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
                    break;
                }
                case "SELL": {
                    AbsSystem.sellLoan(loan);
                    ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
                    break;
                }
            }
        }
        catch (Exception e) {
            response.getWriter().println(ServletUtils.GSON.toJson(e.getMessage()));
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        finally {
            response.getWriter().close();
        }
    }

}
