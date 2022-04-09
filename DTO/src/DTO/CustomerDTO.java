package DTO;

import java.util.ArrayList;

public class CustomerDTO {

    private String customerName;
    private ArrayList<AccountMovementDTO> accountMovements;
    private ArrayList<LoanDTO> loansAsBorrower;
    private ArrayList<LoanDTO> loansAsLender;
    private double balance;

    public CustomerDTO(String name, double balance)
    {
        this.customerName = name;
        this.balance = balance;
    }

    public String getCustomerName() { return customerName; }
    public ArrayList<AccountMovementDTO> getAccountMovements() { return accountMovements; }
    public ArrayList<LoanDTO> getLoansAsBorrower() { return loansAsBorrower; }
    public ArrayList<LoanDTO> getLoansAsLender() { return loansAsLender; }
    public double getBalance() { return balance; }

    public void setAccountMovements(ArrayList<AccountMovementDTO> accountMovements) {
        this.accountMovements = accountMovements;
    }

    @Override
    public String toString()
    {
        String toReturn = new String();
        toReturn = "Customer name:" + customerName + "\n";

        int i = 1;
        for (AccountMovementDTO m : accountMovements)
        {
            toReturn += (i + ". " + accountMovements.toString() + "\n");
        }

        toReturn += "\n" + "Loans as borrower:\n";

        for (LoanDTO l : loansAsBorrower)
        {
            toReturn+=toStringCustomerLoans(l);
        }

        toReturn += "\n" + "Loans as lender:\n";

        for (LoanDTO l : loansAsLender)
        {
            toReturn+=toStringCustomerLoans(l);
        }

        return toReturn;
    }

    public String toStringCustomerLoans(LoanDTO l)
    {
        String toReturn= new String();
        toReturn += l.LoanBasicInfoToString();

        switch(l.getStatus())
        {
            case PENDING:
            {
                double amountMissing = l.getInitialAmount() -  l.getTotalMoneyRaised();
                toReturn += "Loan needs " + amountMissing + " more to become active";
                break;
            }
            case ACTIVE:
            {
                toReturn += ("Next payment in yaz number " + l.findNextPaymentYaz() + "\n" +
                        "The amount of the next payment is: "
                        + l.findPaymentTotalAmount(l.getUnpaidPayments().get(l.getUnpaidPayments().firstKey())) + "\n");
                break;
            }
            case IN_RISK:
            {
                toReturn+= ("There are " + l.getUnpaidPayments().size() + "unpayed payments in total amount of "
                        + l.getDebt() + "\n");
                break;
            }
            case FINISHED:
            {
                toReturn += ("Start yaz: " + l.getActivationYaz() + "\nFinish yaz: " + l.getFinishYaz());
                break;
            }
        }

        return toReturn;
    }
    }

}