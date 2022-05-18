package DTO;

import java.util.ArrayList;

public class CustomerDTO
{
    private String customerName;
    private ArrayList<AccountMovementDTO> accountMovements;
    private ArrayList<LoanDTO> loansAsBorrower;
    private ArrayList<LoanDTO> loansAsLender;
    private double balance;

    public CustomerDTO(String name, double balance)
    {
        this.customerName = name;
        this.balance = balance;
        loansAsBorrower = new ArrayList<LoanDTO>();
        loansAsLender = new ArrayList<LoanDTO>();
        accountMovements = new ArrayList<AccountMovementDTO>();
    }

    public String getCustomerName() { return customerName; }
    public ArrayList<AccountMovementDTO> getAccountMovements() { return accountMovements; }
    public ArrayList<LoanDTO> getLoansAsBorrower() { return loansAsBorrower; }
    public ArrayList<LoanDTO> getLoansAsLender() { return loansAsLender; }
    public double getBalance() { return balance; }

    //setters
    public void setAccountMovements(ArrayList<AccountMovementDTO> accountMovements) {
        this.accountMovements = accountMovements;
    }
    public void setLoansAsLender(ArrayList<LoanDTO> loans){
        this.loansAsLender = loans;
    }

    public void setLoansAsBorrower(ArrayList<LoanDTO> loans){
        this.loansAsBorrower = loans;
    }

    public int getAmountOfLenderLoans() { return loansAsLender.size(); }
    public int getAmountOfLoanerLoans() { return loansAsBorrower.size(); }

}