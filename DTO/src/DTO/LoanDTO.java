package DTO;

import Engine.ABSsystem.Loan;

import java.util.ArrayList;
import java.util.SortedMap;

public class LoanDTO {

    private class PaymentDTO{

        private final int originalYazToPay;
        private int paymentYaz;
        private double loanPayment;
        private double InterestPayment;
        private boolean isPayed;

        public PaymentDTO(int originalYaz, double loanPayment, double interestPayment) {
            this.originalYazToPay = originalYaz;
            this.loanPayment = loanPayment;
            this.InterestPayment = interestPayment;
        }
    }

    private final int loanId;
    private final String customerName;
    private final double initialAmount;
    private final int maxYazToPay;
    private final double interestPerPayment;
    private final int yazPerPayment;
    private final Loan.LoanCategory category;
    private Loan.LoanStatus status;

    //pending info
    private ArrayList<String[]> lendersNameAndAmount;
    private double totalMoneyRaised;

    //active info
    private int ActivationYaz;
    private int nextPaymentYaz;

    private SortedMap<Integer ,PaymentDTO> payments;


    public LoanDTO(int id, String custName, double initialAmount, int totalYaz, double interest, int yazPerPayment, Loan.LoanCategory category,
                   Loan.LoanStatus status, ArrayList<String[]> lendersAndAmounts)
    {
        loanId = id;
        customerName = custName;
        this.initialAmount = initialAmount;
        maxYazToPay = totalYaz;
        this.interestPerPayment = interest;
        this.yazPerPayment = yazPerPayment;
        this.category = category;
        this.status = status;
        this.lendersNameAndAmount = lendersAndAmounts;
    }

    //getters
    public int getLoanId() {
        return loanId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public double getInitialAmount() {
        return initialAmount;
    }
    public int getMaxYazToPay() {
        return maxYazToPay;
    }
    public double getInterestPerPayment() { return interestPerPayment; }
    public int getYazPerPayment() { return yazPerPayment; }
    public Loan.LoanCategory getCategory() {
        return category;
    }
    public Loan.LoanStatus getStatus() {
        return status;
    }
    public ArrayList<String[]> getLendersNamesAndAmounts() {
        return lendersNameAndAmount;
    }
    public double getTotalMoneyRaised() {
        return totalMoneyRaised;
    }
    public int getActivationYaz() {
        return ActivationYaz;
    }
    public int getNextPaymentYaz() {
        return nextPaymentYaz;
    }
    public SortedMap<Integer, PaymentDTO> getPayments;

    //setters
    public void setStatus(Loan.LoanStatus status) {
        this.status = status;
    }
    public void setTotalMoneyRaised(double totalMoneyRaised) {
        this.totalMoneyRaised = totalMoneyRaised;
    }
    public void setActivationYaz(int activationYaz) {
        ActivationYaz = activationYaz;
    }
    public void setNextPaymentYaz(int nextPaymentYaz) {
        this.nextPaymentYaz = nextPaymentYaz;
    }

    public void addPayment(int originalYaz, double originalLoan, double originalInterest) {
        LoanDTO.PaymentDTO p = new LoanDTO.PaymentDTO(originalYaz, originalLoan, originalInterest);
        payments.put(p.originalYazToPay, p);
    }





}