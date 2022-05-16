package DTO;

import Engine.Loan;
import Engine.LoanPaymentsData;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableMapValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class newLoanDTO {

    public class PaymentDTO
    {
        private final int originalYazToPay;
        private final int actualPaymentYaz;
        private final double loanPayment;
        private final double interestPayment;
        private final String paymentType;

        public PaymentDTO(int originalYaz, double loanPayment, double interestPayment, int actualYaz, String type) {
            this.originalYazToPay = originalYaz;
            this.loanPayment = loanPayment;
            this.interestPayment = interestPayment;
            this.actualPaymentYaz = actualYaz;
            this.paymentType = type;
        }

        public int getOriginalYazToPay() { return originalYazToPay; }
        public int getActualPaymentYaz() { return actualPaymentYaz; }
        public double getLoanPayment() { return loanPayment; }
        public double getInterestPayment() { return interestPayment; }
    }

    public static class LenderDetailsDTO {

        public String lenderName;
        public double lendersInvestAmount;

        public LenderDetailsDTO(String lender, double amount)
        {
            lenderName = lender;
            lendersInvestAmount = amount;
        }
    }

    private final SimpleStringProperty loanName;
    private final SimpleStringProperty customerName;
    private final SimpleDoubleProperty initialAmount;
    private final SimpleIntegerProperty maxYazToPay;
    private final SimpleDoubleProperty interestPerPayment;
    private final SimpleDoubleProperty totalInterest;
    private final SimpleIntegerProperty yazPerPayment;
    private final SimpleStringProperty category;
    private final SimpleStringProperty status;
    private final SimpleDoubleProperty debt;
    private final SimpleDoubleProperty paidInterest;
    private final SimpleDoubleProperty paidLoan;

    private final SortedMap<Integer, newLoanDTO.PaymentDTO> unpaidPayments;
    private final SortedMap<Integer, newLoanDTO.PaymentDTO> paidPayments;


    //pending info
    private final ArrayList<LoanDTO.LenderDetailsDTO> lendersNameAndAmount;
    private SimpleDoubleProperty totalMoneyRaised;

    //active info
    private SimpleIntegerProperty activationYaz;
    private SimpleIntegerProperty nextPaymentYaz;

    //finish info
    private SimpleIntegerProperty finishYaz;




    public newLoanDTO(String loanName, String custName, double initialAmount, int totalYaz, double interestPerPayment, double totalInterest, int yazPerPayment,
                   String status, String category, double paidInterest, double paidLoan, double debt)
    {
        this.loanName = new SimpleStringProperty(loanName);
        this.customerName = new SimpleStringProperty(custName);
        this.initialAmount = new SimpleDoubleProperty(initialAmount);
        this.maxYazToPay = new SimpleIntegerProperty(totalYaz);
        this.interestPerPayment = new SimpleDoubleProperty(interestPerPayment);
        this.totalInterest = new SimpleDoubleProperty(totalInterest);
        this.yazPerPayment = new SimpleIntegerProperty(yazPerPayment);
        this.category = new SimpleStringProperty(category);
        this.status = new SimpleStringProperty(status);
        this.paidInterest = new SimpleDoubleProperty(paidInterest);
        this.paidLoan = new SimpleDoubleProperty(paidLoan);
        this.debt = new SimpleDoubleProperty(debt);

        unpaidPayments = new TreeMap<>();
        paidPayments = new TreeMap<>();
        lendersNameAndAmount = new ArrayList<>();
    }

    //getters
    public String getLoanName() {
        return loanName.get();
    }
    public String getCustomerName() {
        return customerName.get();
    }
    public double getInitialAmount() {
        return initialAmount.get();
    }
    public int getMaxYazToPay() {
        return maxYazToPay.get();
    }
    public double getInterestPerPayment() {
        return interestPerPayment.get();
    }
    public double getTotalInterest() {
        return totalInterest.get();
    }
    public int getYazPerPayment() {
        return yazPerPayment.get();
    }
    public String getCategory() {
        return category.get();
    }
    public String getStatus() {
        return status.get();
    }

    public void addToLendersNameAndAmount(String lender, double amount)
    {
        LoanDTO.LenderDetailsDTO ld = new LoanDTO.LenderDetailsDTO(lender, amount);
        lendersNameAndAmount.add(ld);
    }

    public void setUnpaidPayments(Engine.PaymentsDB.PaymentsDB payments)
    {
        for(LoanPaymentsData.Payment p : payments.getPayments().values())
        {
            newLoanDTO.PaymentDTO payment = new newLoanDTO.PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
                    p.getInterestPartOfThePayment(), p.getActualPaymentYaz(), p.getPaymentType().toString());

            unpaidPayments.put(payment.originalYazToPay, payment);
        }
    }

    public void setPaidPayments(Engine.PaymentsDB.PaymentsDB payments)
    {
        for(LoanPaymentsData.Payment p : payments.getPayments().values())
        {
            newLoanDTO.PaymentDTO payment = new newLoanDTO.PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
                    p.getInterestPartOfThePayment(), p.getActualPaymentYaz(), p.getPaymentType().toString());

            paidPayments.put(payment.actualPaymentYaz, payment);
        }
    }

}
