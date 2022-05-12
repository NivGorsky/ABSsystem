package Engine;

import DTO.*;
import Exceptions.XMLFileException;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;

public interface MainSystem {

    public void loadXML(String path) throws XMLFileException, JAXBException;
    public ArrayList<LoanDTO> showLoansInfo();
    public ArrayList<CustomerDTO> showCustomersInfo();
    public void depositMoney(String customerName, double amount);
    public void withdrawMoney(String customerName, double amount) throws Exception;
    public void assignLoansToLender(LoanPlacingDTO dto) throws Exception;
    public TimelineDTO moveTimeLine();
    public ArrayList<String> getCustomersNames();
    public int getCurrYaz();
    public ArrayList<String> getSystemLoanCategories();

    //new methods for javafx
    public ArrayList<AccountMovementDTO> showCustomerMovements(String customerName);
}
