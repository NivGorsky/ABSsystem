package Engine;
import java.io.Console;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import DTO.*;
import Engine.LoanPlacing.LoanPlacing;
import Engine.TimeLineMoving.MoveTimeLine;
import Exceptions.DataBaseAccessException;

public class ABSsystem implements MainSystem, SystemService{

    //need to change data structures
    private Timeline systemTimeline;
    private Map<String ,Customer> name2customer;
    private Map<Loan.LoanStatus, Loan> status2loan;
    private LinkedList<Loan> loans;
    private LinkedList<Loan> activeLoans;
    private Map<String, Loan> borrowerName2ActiveLoans;
    private Map<String, Loan> loanId2Loan;

    public ABSsystem()
    {
        systemTimeline = new Timeline();
        borrowerName2ActiveLoans = new TreeMap<String, Loan>();
    }

    @Override
    public int getCurrYaz() { return systemTimeline.getCurrentYaz();}

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
        try {
            if (status2loan != null) {
                ArrayList<LoanDTO> LoansInfo = new ArrayList<LoanDTO>();


                for (Loan l : status2loan.values()) {
                    LoanDTO currLoan = createLoanDTO(l);

                    switch (l.getStatus()) {
                        case ACTIVE: {
                            if (l.getPayedPayments() != null) {
                                for (l.getPayedPayments())
                            }

                            break;
                        }
                    }

                }
            } else {
                return null;
            }

            return null;
        }

        catch (RuntimeException e1){ //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }
    }

    @Override
    public ArrayList<CustomerDTO> showCustomersInfo() //TODO
    {
        return null;
    }

    @Override
    public void depositMoney(String customerName, double amount)
    {
        try {
            Customer chosenCustomer = name2customer.get(customerName);
            chosenCustomer.depositMoney(systemTimeline.getCurrentYaz(), amount);
        }

        catch (RuntimeException e1){ //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void withdrawMoney(String customerName, double amount) throws Exception {
        Customer chosenCustomer = name2customer.get(customerName);
        try {
            chosenCustomer.withdrawMoney(systemTimeline.getCurrentYaz(), amount);
        }

        catch (RuntimeException e1){ //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void assignLoansToLender(LoanPlacingDTO loanPlacingDTO){
        try {
            LoanPlacing.placeToLoans(loanPlacingDTO, this.activeLoans, this);

        }

        catch (RuntimeException e1){ //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void moveTimeLine()
    {
        try {
            MoveTimeLine.moveTimeLineInOneYaz(this, systemTimeline);
        }

        catch (RuntimeException e1){ //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }
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
        try {
            accountToSubtract.substructFromBalance(this.getCurrYaz(), amount);
            accountToAdd.addToBalance(this.getCurrYaz(), amount);
        }

        catch (RuntimeException e1){ //user exceptions will be catched in UI
            System.out.println(e1.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Customer getCustomerByName(String name){return name2customer.get(name);}

    @Override
    public Map<String, Customer> getAllCustomers(){
        return this.name2customer;
    }

    @Override
    public Timeline getTimeLine(){
        return this.systemTimeline;
    }



}
