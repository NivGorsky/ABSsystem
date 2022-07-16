import DTO.LoanDTO;
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

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet
{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        MainSystem engine = ServletUtils.getAbsSystem(getServletContext());

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        UIPaymentDTO uiPaymentDTO = gson.fromJson(reader, UIPaymentDTO.class);

        try{
            handleRequest(uiPaymentDTO, engine);
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

    protected void handleRequest(UIPaymentDTO uiPaymentDTO, MainSystem engine) throws Exception{
        switch (uiPaymentDTO.operation){
            case "payDebt":
                synchronized (this){
                    handlePayDebt(uiPaymentDTO, engine);
                }

                break;

            case "closeLoan":
                synchronized (this){
                    handleCloseLoan(uiPaymentDTO, engine);
                }

                break;

            case "payToAllLenders":
                synchronized (this){
                    handlePayToAllLenders(uiPaymentDTO, engine);
                }

                break;

            case "payToLender":
                synchronized (this){
                    handlePayToLender(uiPaymentDTO, engine);
                }

                break;
        }
    }

    protected void handlePayToLender(UIPaymentDTO uiPaymentDTO, MainSystem engine) throws Exception {
        engine.payToLender(uiPaymentDTO.lenderDetailsDTO, uiPaymentDTO.loanDTO, uiPaymentDTO.yaz);
    }

    protected void handlePayToAllLenders(UIPaymentDTO uiPaymentDTO, MainSystem engine) throws Exception{
        engine.payToAllLendersForCurrentYaz(uiPaymentDTO.loanDTO, uiPaymentDTO.yaz);
    }

    protected void handleCloseLoan(UIPaymentDTO uiPaymentDTO, MainSystem engine) throws Exception{
        double loanAmountToPay = uiPaymentDTO.amount;
        String customerName = uiPaymentDTO.customerName;

        if(engine.getCustomerDTO(customerName).getBalance() >= loanAmountToPay){
            engine.closeLoan(uiPaymentDTO.loanDTO, uiPaymentDTO.yaz);
        }

        else{
            throw new Exception("Insufficient funds for closing the load");
        }
    }

    protected void handlePayDebt(UIPaymentDTO uiPaymentDTO, MainSystem engine) throws Exception{
        engine.payDebt(uiPaymentDTO.amount,uiPaymentDTO.loanDTO, uiPaymentDTO.yaz);
    }
}
