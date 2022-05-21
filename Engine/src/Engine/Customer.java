package Engine;

import Exceptions.ValueOutOfRangeException;
import java.util.ArrayList;

public class Customer implements Comparable{

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

    public boolean equals(Object o){
        return (o instanceof Customer && ((Customer)o).getName().equals(this.getName()));
    }

    public int hashCode(){
        return this.getName().hashCode();
    }

    public void depositMoney(int yaz, double amount)
    {
        account.addToBalance(yaz, amount);
    }

    public void addLoanAsLender(Loan loan){
        this.loansAsLender.add(loan);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Customer)){
            return -1;
        }

        else{
            return this.getName().compareTo(((Customer) o).getName());
        }
    }
}
