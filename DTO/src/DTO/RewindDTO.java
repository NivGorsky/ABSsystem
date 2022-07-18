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

    public void setCurrentYaz(int currentYaz) {
        this.currentYaz = currentYaz;
    }

    public void setCustomers(ArrayList<CustomerDTO> customers) {
        this.customers = customers;
    }

    public void setLoans(ArrayList<LoanDTO> loans) {
        this.loans = loans;
    }

    public CustomerDTO findCustomer(String customerName) {
        CustomerDTO toReturn = null;
        for(CustomerDTO customer : customers) {
            if(customer.getCustomerName() == customerName) {
                toReturn = customer;
                break;
            }
        }

        return toReturn;
    }

}
