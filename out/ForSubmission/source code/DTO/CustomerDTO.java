package DTO;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class CustomerDTO
{
    private SimpleStringProperty customerName;
    private ArrayList<AccountMovementDTO> accountMovements;
    private ArrayList<LoanDTO> loansAsBorrower;
    private ArrayList<LoanDTO> loansAsLender;
    private SimpleDoubleProperty balance;

    public CustomerDTO(String name, double balance)
    {
        this.customerName = new SimpleStringProperty(name) ;
        this.balance = new SimpleDoubleProperty(balance);
        loansAsBorrower = new ArrayList<LoanDTO>();
        loansAsLender = new ArrayList<LoanDTO>();
        accountMovements = new ArrayList<AccountMovementDTO>();
    }

    public String getCustomerName() { return customerName.get(); }
    public SimpleStringProperty getCustomerNameProperty() { return customerName; }


    public ArrayList<AccountMovementDTO> getAccountMovements() { return accountMovements; }

    public ArrayList<LoanDTO> getLoansAsBorrower() { return loansAsBorrower; }

    public ArrayList<LoanDTO> getLoansAsLender() { return loansAsLender; }

    public double getBalance() { return balance.get(); }
    public SimpleDoubleProperty getBalanceProperty() { return balance; }

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

    public SimpleIntegerProperty getAmountOfLenderLoansProperty() {
        return new SimpleIntegerProperty(loansAsLender.size());
    }

    public SimpleIntegerProperty getAmountOfLoansPropertyPerStatusAsLoaner(String status)
    {
        int count = 0;

        for(LoanDTO l : loansAsBorrower)
        {
            if(l.getStatus().equals(status))
            {
                count++;
            }
        }

        return new SimpleIntegerProperty(count);
    }

    public SimpleIntegerProperty getAmountOfLoansPropertyPerStatusAsLender(String status)
    {
        int count = 0;

        for(LoanDTO l : loansAsLender)
        {
            if(l.getStatus().equals(status))
            {
                count++;
            }
        }

        return new SimpleIntegerProperty(count);
    }




    public SimpleIntegerProperty getAmountOfLoanerLoansProperty() {
        return new SimpleIntegerProperty(loansAsBorrower.size());
    }



}