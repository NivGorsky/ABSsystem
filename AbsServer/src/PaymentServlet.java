import DTO.UIPaymentDTO;
import Engine.MainSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jsonDeserializer.GsonWrapper;

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
        UIPaymentDTO uiPaymentDTO = GsonWrapper.GSON.fromJson(reader, UIPaymentDTO.class);

        try {
            synchronized (this) {
                handleRequest(uiPaymentDTO, engine);
            }

            ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
            ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
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
            case "payDebt": {
                handlePayDebt(uiPaymentDTO, engine);
                break;
            }

            case "closeLoan": {
                handleCloseLoan(uiPaymentDTO, engine);
                break;
            }



            case "payToAllLenders": {
                handlePayToAllLenders(uiPaymentDTO, engine);
                break;
            }

            case "payToLender": {
                handlePayToLender(uiPaymentDTO, engine);
                break;
            }
        }

        ServletUtils.setAdminVersion(ServletUtils.getAdminVersion() + 1);
        ServletUtils.setCustomerVersion(ServletUtils.getCustomerVersion() + 1);
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
