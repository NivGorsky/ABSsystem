package Engine;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import DTO.*;

public class ABSsystem implements MainSystem, SystemService{

    //need to change data structures
    private Timeline systemTimeline;
    private Map<String ,Customer> name2customer;
    private Map<Loan.LoanStatus, Loan> status2loan;
    private LinkedList<Loan> loans;
    private LinkedList<Loan> activeLoans;
    //private Map<String, Loan> borrowerName2ActiveLoans;
    private Map<String, Loan> loanId2Loan;

    public ABSsystem()
    {
        systemTimeline = new Timeline();
        borrowerName2ActiveLoans = new TreeMap<String, Loan>();
    }

    public int getCurrYaz() { return systemTimeline.getCurrentYaz();
    }

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
                LoanDTO currLoan = createLoanDTO(l);

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
    public void assignLoansToLender(LoanPlacingDTO loanPlacingDTO){
        LoanPlacing.LoanPlacementStatus result = LoanPlacing.placeToLoans(loanPlacingDTO, this.activeLoans, this);
    }

    @Override
    public void moveTimeline()
    {



    }

    public LoanDTO createLoanDTO(Loan l)
    {
        LoanDTO loan = new LoanDTO(l.getLoanId(), l.getBorrowerName(), l.getInitialAmount(),
                l.getMaxYazToPay(), l.getInterestPerPaymentSetByBorrowerInPercents(), l.getPaymentRateInYaz(),
                l.getStatus().toString(), l.getCategory().toString());

        for(Loan.LenderDetails ld : l.getLendersDetails())
        {
            loan.addToLendersNameAndAmount(ld.lender.getName(), ld.lendersAmount);
        }

    }

    //system service interface
    @Override
    public void moveMoneyBetweenAccounts(Account accountToSubtract, Account accountToAdd, double amount){
        accountToSubtract.substructFromBalance(this.getCurrYaz(), amount);
        accountToAdd.addToBalance(this.getCurrYaz(), amount);
    }

    @Override
    public Customer getCustomerByName(String name){return name2customer.get(name);}



}
