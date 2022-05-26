package Engine;

import DTO.*;
import Exceptions.XMLFileException;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.function.Consumer;

import customer.scramble.ScrambleController;

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
    public LoanCategorisDTO getSystemLoanCategories();

    //new methods for javafx
    public CustomerDTO getCustomerDTO(String customerName);
    public NotificationsDTO getNotificationsDTO(String customerName);
    public ArrayList<LoanDTO> getLoansByCustomerNameAsBorrower(String customerName);
    public ArrayList<LoanDTO> getLoansByCustomerNameAsLender(String customerName);
//    public ScrambleQueryFieldsDTO getScrambleQueryFields();
    public void setScrambleController(ScrambleController controller);
    public void assignLoansToLenderWithTask(LoanPlacingDTO loanPlacingDTO, Consumer<Integer> numberOfLoansAssigned);




}
