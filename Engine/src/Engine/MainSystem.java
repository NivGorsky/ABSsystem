package Engine;
import DTO.*;
import Exceptions.XMLFileException;
import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.function.Consumer;


public interface MainSystem {

     void loadXML(String contentType, InputStream inputStream, String customer) throws XMLFileException, JAXBException;
     ArrayList<LoanDTO> showLoansInfo();
     ArrayList<CustomerDTO> showCustomersInfo();
     void depositMoney(String customerName, double amount);
     void withdrawMoney(String customerName, double amount) throws Exception;
     void assignLoansToLender(LoanPlacingDTO dto) throws Exception;
     TimelineDTO moveTimeLine();
     ArrayList<String> getCustomersNames();
     int getCurrYaz();
     LoanCategorisDTO getSystemLoanCategories();

    //new methods for javafx
     CustomerDTO getCustomerDTO(String customerName);
     NotificationsDTO getNotificationsDTO(String customerName);
     ArrayList<LoanDTO> getLoansByCustomerNameAsBorrower(String customerName);
     ArrayList<LoanDTO> getLoansByCustomerNameAsLender(String customerName);
//    public ScrambleQueryFieldsDTO getScrambleQueryFields();
     void setScrambleController(UIController controller);
     void assignLoansToLenderWithTask(LoanPlacingDTO loanPlacingDTO, Consumer<Integer> numberOfLoansAssigned);
     void closeLoan(LoanDTO loanDTO, int yaz) throws Exception;
     void payToAllLendersForCurrentYaz(LoanDTO loan, int yaz) throws Exception;
     void payToLender(LoanDTO.LenderDetailsDTO lenderDTO, LoanDTO loanDTO, int yaz) throws Exception;
     boolean hasBorrowerEnoughFundsToPayAmount(Customer customerDTO, double amount);
     void payDebt(Double amount, LoanDTO loanDTO, int yaz) throws Exception;

    /*services:
    void registerAsCustomer(String name) throws Exception
    void registerAsAdmin(String name) throws Exception
    List<String> getCategories(); */

    boolean isCustomerExists(String name);
    void addCustomer(String name);
    boolean isAdminExists(String name);
    boolean isAdminLoggedIn(String name);
    void addAdmin(String name);






}
