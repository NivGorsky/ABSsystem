package Engine;

import Exceptions.ValueOutOfRangeException;
import java.util.ArrayList;

public class Customer {

    private final String name;
    private ArrayList<Loan> loansAsBorrower;
    private ArrayList<Loan> loansAsLender;
    private final Account account;

    public Customer (String custName, double balance)
    {
        this.name = custName;
        this.account = new Account(balance);
        loansAsBorrower = new ArrayList<>();
        loansAsLender = new ArrayList<>();
    }

    public String getName() { return name; }
    public ArrayList<Loan> getLoansAsBorrower() { return loansAsBorrower; }
    public ArrayList<Loan> getLoansAsLender() { return loansAsLender; }
    public Account getAccount() { return account; }

    public void withdrawMoney(int yaz, double amount) throws ValueOutOfRangeException
    {
        if(account.getBalance() < amount)
        {
            throw new ValueOutOfRangeException(0, account.getBalance(), "You are trying to withdraw more money than you have in balance!");
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

    public void addLoanAsLender(Loan loan){
        this.loansAsLender.add(loan);
    }
}
