//package DTO;
//
//import Engine.Loan;
//import Engine.LoanPaymentsData;
//import javafx.beans.InvalidationListener;
//import javafx.beans.property.Property;
//import javafx.beans.property.SimpleDoubleProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableMapValue;
//import javafx.beans.value.ObservableValue;
//import javafx.collections.ObservableMap;
//
//import java.util.ArrayList;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//public class newLoanDTO {
//
//    public class PaymentDTO
//    {
//        private final int originalYazToPay;
//        private final int actualPaymentYaz;
//        private final double loanPayment;
//        private final double interestPayment;
//        private final String paymentType;
//
//        public PaymentDTO(int originalYaz, double loanPayment, double interestPayment, int actualYaz, String type) {
//            this.originalYazToPay = originalYaz;
//            this.loanPayment = loanPayment;
//            this.interestPayment = interestPayment;
//            this.actualPaymentYaz = actualYaz;
//            this.paymentType = type;
//        }
//
//        public int getOriginalYazToPay() { return originalYazToPay; }
//        public int getActualPaymentYaz() { return actualPaymentYaz; }
//        public double getLoanPayment() { return loanPayment; }
//        public double getInterestPayment() { return interestPayment; }
//    }
//
//    public static class LenderDetailsDTO {
//
//        public String lenderName;
//        public double lendersInvestAmount;
//
//        public LenderDetailsDTO(String lender, double amount)
//        {
//            lenderName = lender;
//            lendersInvestAmount = amount;
//        }
//    }
//
//    private final String loanName;
//    private final String customerName;
//    private final double initialAmount;
//    private final int maxYazToPay;
//    private final double interestPerPayment;
//    private final double totalInterest;
//    private final int yazPerPayment;
//    private final String category;
//    private final Loan.LoanStatus status;
//    private final double debt;
//    private final double paidInterest;
//    private final double paidLoan;
//    private final SortedMap<Integer, LoanDTO.PaymentDTO> unpaidPayments;
//    private final SortedMap<Integer, LoanDTO.PaymentDTO> paidPayments;
//
//    //pending info
//    private final ArrayList<LoanDTO.LenderDetailsDTO> lendersNameAndAmount;
//    private double totalMoneyRaised;
//
//    //active info
//    private int activationYaz;
//    private int nextPaymentYaz;
//
//    //finish info
//    private int finishYaz;
//
//
//    public LoanDTO(String loanName, String custName, double initialAmount, int totalYaz, double interestPerPayment, double totalInterest, int yazPerPayment,
//                   Loan.LoanStatus status, String category, double paidInterest, double paidLoan, double debt)
//    {
//        this.loanName = loanName;
//        this.customerName = custName;
//        this.initialAmount = initialAmount;
//        this.maxYazToPay = totalYaz;
//        this.interestPerPayment = interestPerPayment;
//        this.totalInterest = totalInterest;
//        this.yazPerPayment = yazPerPayment;
//        this.category = category;
//        this.status = status;
//        this.paidInterest = paidInterest;
//        this.paidLoan = paidLoan;
//        this.debt = debt;
//
//        unpaidPayments = new TreeMap<>();
//        paidPayments = new TreeMap<>();
//        lendersNameAndAmount = new ArrayList<>();
//    }
//
//    //getters
//    public String getCustomerName() {
//        return customerName;
//    }
//    public double getInitialAmount() {
//        return initialAmount;
//    }
//    public int getMaxYazToPay() {
//        return maxYazToPay;
//    }
//    public double getInterestPerPayment() { return interestPerPayment; }
//    public int getYazPerPayment() { return yazPerPayment; }
//    public String getCategory() {
//        return category;
//    }
//    public Loan.LoanStatus getStatus() {
//        return status;
//    }
//    public ArrayList<LoanDTO.LenderDetailsDTO> getLendersNamesAndAmounts() {
//        return lendersNameAndAmount;
//    }
//    public double getTotalMoneyRaised() {
//        return totalMoneyRaised;
//    }
//    public int getActivationYaz() {
//        return activationYaz;
//    }
//    public int getNextPaymentYaz() {
//        return nextPaymentYaz;
//    }
//    public SortedMap<Integer, LoanDTO.PaymentDTO> getUnpaidPayments() { return unpaidPayments; }
//    public SortedMap<Integer, LoanDTO.PaymentDTO> getPaidPayments() { return paidPayments; }
//    public double getDebt() { return debt; }
//    public int getFinishYaz() { return finishYaz; }
//
//    //setters
//    public void setTotalMoneyRaised(double totalMoneyRaised) {
//        this.totalMoneyRaised = totalMoneyRaised;
//    }
//    public void setActivationYaz(int activationYaz) {
//        this.activationYaz = activationYaz;
//    }
//    public void setNextPaymentYaz(int nextPaymentYaz) {
//        this.nextPaymentYaz = nextPaymentYaz;
//    }
//    public void setFinishYaz(int finishYaz) { this.finishYaz = finishYaz; }
//
//
//    public void addToLendersNameAndAmount(String lender, double amount)
//    {
//        LoanDTO.LenderDetailsDTO ld = new LoanDTO.LenderDetailsDTO(lender, amount);
//        lendersNameAndAmount.add(ld);
//    }
//
//    public int findNextPaymentYaz() {
//        return unpaidPayments.get(unpaidPayments.firstKey()).getOriginalYazToPay();
//    }
//
//    public double findPaymentTotalAmount(LoanDTO.PaymentDTO p)
//    {
//        return (p.loanPayment + p.interestPayment);
//    }
//
//    public double findLoanAndInterestTotalAmount()
//    {
//        double sum=0;
//        for(LoanDTO.PaymentDTO p : unpaidPayments.values())
//        {
//            sum += findPaymentTotalAmount(p);
//        }
//
//        for(LoanDTO.PaymentDTO p : paidPayments.values())
//        {
//            sum += findPaymentTotalAmount(p);
//        }
//
//        return sum;
//    }
//
//    public void setUnpaidPayments(Engine.PaymentsDB.PaymentsDB payments)
//    {
//        for(LoanPaymentsData.Payment p : payments.getPayments().values())
//        {
//            LoanDTO.PaymentDTO payment = new LoanDTO.PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
//                    p.getInterestPartOfThePayment(), p.getActualPaymentYaz(), p.getPaymentType().toString());
//
//            unpaidPayments.put(payment.originalYazToPay, payment);
//        }
//    }
//
//    public void setPaidPayments(Engine.PaymentsDB.PaymentsDB payments)
//    {
//        for(LoanPaymentsData.Payment p : payments.getPayments().values())
//        {
//            LoanDTO.PaymentDTO payment = new LoanDTO.PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
//                    p.getInterestPartOfThePayment(), p.getActualPaymentYaz(), p.getPaymentType().toString());
//
//            paidPayments.put(payment.actualPaymentYaz, payment);
//        }
//    }
//
//    @Override
//    public String toString() {
//
//        String toReturn = "";
//        toReturn += LoanBasicInfoToString();
//
//        switch(status)
//        {
//            case PENDING:
//            {
//                toReturn += lendersNameAndAmountToString();
//                double amountMissing = initialAmount -  totalMoneyRaised;
//                toReturn += ("The amount raised so far is: " + totalMoneyRaised +
//                        "\nLoan needs " + amountMissing + " more to become active\n");
//                break;
//            }
//            case ACTIVE:
//            {
//                toReturn += lendersNameAndAmountToString();
//                toReturn += loanActiveInfoToString();
//                break;
//            }
//            case IN_RISK:
//            {
//                toReturn += lendersNameAndAmountToString();
//                toReturn += loanActiveInfoToString();
//                toReturn += delayedPaymentsToString();
//
//                break;
//            }
//            case FINISHED:
//            {
//                toReturn += lendersNameAndAmountToString();
//                toReturn+=("Start yaz: " + activationYaz + "\nFinish yaz: " +finishYaz);
//                toReturn+=("All paid payments:\n");
//                toReturn += paidPaymentsToString();
//
//                break;
//            }
//        }
//        return toReturn;
//    }
//
//    private String loanActiveInfoToString()
//    {
//        String toReturn = "";
//
//        toReturn += ("The loan became active in yaz number " + activationYaz +
//                "\nNext payment in yaz number " + findNextPaymentYaz() + "\n" +
//                "All payments made so far:\n");
//
//        toReturn += paidPaymentsToString();
//
//        toReturn+= ("Total loan paid: " + paidLoan + " , remained to pay: " + (initialAmount-paidLoan) +
//                "\nTotal interest paid: " + paidInterest + " , remained to pay: " + (totalInterest-paidInterest) + "\n");
//
//        return toReturn;
//    }
//
//    public String LoanBasicInfoToString()
//    {
//        String toReturn = "";
//
//        toReturn += "Loan name: " + loanName + "\n" +
//                "Loan category: " + category + "\n" +
//                "Original loan amount: " + initialAmount + "\n" +
//                "Total time to repay the loan: " + maxYazToPay +"\n" +
//                "Payment will be made every " + yazPerPayment + " yaz\n" +
//                "Interest per payment: " + interestPerPayment + "%\n" +
//                "Total loan amount to pay (including interest): " +
//                findLoanAndInterestTotalAmount() + "\n" +
//                "The loan is in status: " + status + "\n";
//
//        return toReturn;
//    }
//
//    private String lendersNameAndAmountToString()
//    {
//        String toReturn;
//        toReturn = "Registered lenders for loan:\n";
//        for(LoanDTO.LenderDetailsDTO ld : lendersNameAndAmount)
//        {
//            toReturn += ("Name: " + ld.lenderName + " Amount invested: " + ld.lendersInvestAmount + "\n");
//        }
//
//        return toReturn;
//    }
//
//    private String delayedPaymentsToString()
//    {
//        String toReturn = "";
//        int i=1;
//
//        toReturn += "All payments that had to be paid and still not paid:\n";
//
//        for (LoanDTO.PaymentDTO p : unpaidPayments.values())
//        {
//            if(p.paymentType.equals( "EXPIRED"))
//            {
//                toReturn += i +". " + p.toString() + "\n";
//                i++;
//            }
//        }
//
//        if(i>1)
//        {
//            toReturn+= "In total " + i + " payments delayed, with total sum of " + debt + "!\n";
//        }
//        return toReturn;
//    }
//
//    private String paidPaymentsToString()
//    {
//        String toReturn = "";
//        int i=1;
//
//        for (LoanDTO.PaymentDTO p : paidPayments.values())
//        {
//            toReturn += i +". " + p.toString() + "\n";
//            i++;
//        }
//
//        return toReturn;
//    }
//
//
//}