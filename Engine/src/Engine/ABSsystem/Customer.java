package Engine.ABSsystem;

import java.util.ArrayList;

public class Customer {

    private final String name;

    private ArrayList<Loan> loansAsBorrower;
    private ArrayList<Loan> loansAsLender;
    private Engine.ABSsystem.Account account;

    public Customer (String custName, double balance)
    {
        this.name = custName;
        this.account = new Engine.ABSsystem.Account(balance);
    }

    public String getName() { return name; }
    public ArrayList<Loan> getLoansAsBorrower() { return loansAsBorrower; }
    public ArrayList<Loan> getLoansAsLender() { return loansAsLender; }

    public void withdrawMoney(int yaz, double amount) throws Exception //TODO - exception class
    {
        if(account.getBalance() < amount)
        {
            throw new Exception("You are trying to withdraw more money than you have in balance!");
        }
        else
        {
            account.substructFromBalance(yaz, amount);
        }
    }

    public void depositMoney(int yaz, double amount)
    {
        account.addToBalance(yaz, amount);
    }
}
