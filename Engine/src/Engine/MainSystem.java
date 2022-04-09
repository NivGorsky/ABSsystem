package Engine;
import DTO.*;
import java.util.ArrayList;

public interface MainSystem {

    public void loadXML(String path) throws Exception;
    public ArrayList<LoanDTO> showLoansInfo();
    public ArrayList<CustomerDTO> showCustomersInfo();
    public void depositMoney(String customerName, double amount);
    public void withdrawMoney(String customerName, double amount) throws Exception;
    public void assignLoansToLender();
    public void moveTimeline();
    public Object getCustomersNames();


}
