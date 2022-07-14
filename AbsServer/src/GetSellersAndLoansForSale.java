import Engine.MainSystem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/sellersAndLoansForSale")
public class GetSellersAndLoansForSale extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("application/json");
        MainSystem AbsSystem = ServletUtils.getAbsSystem(getServletContext());

        ServletUtils.GSON.toJson(AbsSystem.getSeller2loansForSale());
    }
}
