import DTO.LoanDTO;
import DTO.LoanPlacingDTO;
import DTO.UIPaymentDTO;
import Engine.MainSystem;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sun.misc.IOUtils;

import javax.security.auth.login.Configuration;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/loan-placing")
public class LoanPlacingServlet extends HttpServlet
{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        MainSystem engine = ServletUtils.getAbsSystem(getServletContext());

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        LoanPlacingDTO loanPlacingDTO = gson.fromJson(reader, LoanPlacingDTO.class);

        try{
            handleRequest(loanPlacingDTO, engine);
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

    protected void handleRequest(LoanPlacingDTO loanPlacingDTO, MainSystem engine) throws Exception{
        if(engine.getCustomerDTO(loanPlacingDTO.getCustomerName()).getBalance() >= loanPlacingDTO.getAmountToInvest()){
            engine.assignLoansToLender(loanPlacingDTO);
        }

        else{
            throw new Exception("Insufficient funds");
        }
    }
}
