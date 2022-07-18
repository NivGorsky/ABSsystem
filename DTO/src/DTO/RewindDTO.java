package DTO;

import java.util.ArrayList;

public class RewindDTO {

    private int currentYaz;
    private ArrayList<CustomerDTO> customers;
    private ArrayList<LoanDTO> loans;

    public RewindDTO(int yaz, ArrayList<CustomerDTO> customers, ArrayList<LoanDTO> loans) {
        this.currentYaz = yaz;
        this.customers = customers;
        this.loans = loans;
    }

    public int getCurrentYaz() {
        return currentYaz;
    }

    public ArrayList<CustomerDTO> getCustomers() {
        return customers;
    }

    public ArrayList<LoanDTO> getLoans() {
        return loans;
    }
}
