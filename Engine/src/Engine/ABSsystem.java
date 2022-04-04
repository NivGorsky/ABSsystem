package Engine;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import DTO.*;

public class ABSsystem implements MainSystem {

    //need to change data structures
    private Timeline systemTimeline;
    private Map<String ,Customer> name2customer;
    private Map<Loan.LoanStatus, Loan> status2loan;
    private LinkedList<Loan> loans;
    private Map<String, Loan> loanId2Loan;

    public ABSsystem()
    {
        systemTimeline = new Timeline();
        name2customer = new Map<String, Customer>();
        status2loan = new Map<Loan.LoanStatus, Loan>();
    }

    public int getCurrYaz() { return systemTimeline.getCurrentYaz(); }

    @Override
    public Object getCustomersNames()
    {
        return name2customer.keySet().toArray();
    }

    @Override
    public void loadXML() //TODO
    {}

    @Override
    public ArrayList<LoanDTO> showLoansInfo() //TODO
    {
        if (status2loan != null) {
            ArrayList<LoanDTO> LoansInfo = new ArrayList<LoanDTO>();


            for(Loan l : status2loan.values())
            {
                LoanDTO currLoan = new LoanDTO(l.getLoanId(), l.getBorrowerName(), l.getInitialAmount(),
                        l.getMaxYazToPay(), l.getInterestPerPaymentSetByBorrowerInPercents(), l.getPaymentRateInYaz(), l.getCategory(), l.getStatus(), l.getLendersDetails());

                switch (l.getStatus())
                {
                    case ACTIVE:
                    {
                        if(l.getPayedPayments() != null)
                        {
                            for(l.getPayedPayments())
                        }

                        break;
                    }
                }

            }
        }
        else {
            return null;
        }

        return null;
    }

    @Override
    public ArrayList<CustomerDTO> showCustomersInfo() //TODO
    {
        return null;
    }

    @Override
    public void depositMoney(String customerName, double amount)
    {
        Customer chosenCustomer = name2customer.get(customerName);
        chosenCustomer.depositMoney(systemTimeline.getCurrentYaz(), amount);
    }

    @Override
    public void withdrawMoney(String customerName, double amount) throws Exception {
        Customer chosenCustomer = name2customer.get(customerName);
        try {
            chosenCustomer.withdrawMoney(systemTimeline.getCurrentYaz(), amount);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    @Override
    public void assignLoansToLender() {


    }

    @Override
    public void moveTimeline()
    {

    }

}
