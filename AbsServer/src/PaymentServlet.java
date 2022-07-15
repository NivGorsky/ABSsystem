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

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        UIPaymentDTO uiPaymentDTO = gson.fromJson(reader, UIPaymentDTO.class);
        handleRequest(uiPaymentDTO);
    }

    protected void handleRequest(UIPaymentDTO uiPaymentDTO){
        switch (uiPaymentDTO.operation){
            case "payToAllLenders":

                break;

            case "closeLoan":

                break;

            case "payDebt":

                break;

            case "payToLender":

                break;
        }
    }
}
