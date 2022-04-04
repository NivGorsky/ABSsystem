package DTO;

import java.util.ArrayList;

public class CustomerDTO {

    private String customerName;
    private ArrayList<AccountMovementDTO> accountMovements;
    private ArrayList<LoanDTO> loansAsBorrower;
    private ArrayList<LoanDTO> loansAsLender;

    public String getCustomerName() { return customerName; }
    public ArrayList<AccountMovementDTO> getAccountMovements() { return accountMovements; }

    @Override
    public String toString()
    {

        String toReturn = new String();
        toReturn = "ABSsystem.Customer name:" + customerName + "\n";

        int i = 1;
        for (AccountMovementDTO m : accountMovements) {
            toReturn += (i + ". " + accountMovements.toString() + "\n");
        }
        toReturn += "\n" + "Loans as borrower:\n";

        for (LoanDTO l : loansAsBorrower)
        {
            toReturn += "ABSsystem.Loan id: " + l.getLoanId() + "\n" +
                    "ABSsystem.Loan category: " + l.getCategory() + "\n" +
                    "Original loan amount: " + l.getInitialAmount() + "\n" +
                    "Payment Rate: ";
            // toReturn += l.toString();
        }

        toReturn += "\n" + "Loans as lender:\n";

        for (LoanDTO l : loansAsLender) {
            toReturn += l.toString();
        }

        return toReturn;
    }

}