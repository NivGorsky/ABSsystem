package DTO;

import Engine.Loan;
import Engine.LoanPaymentsData;

import java.util.ArrayList;
import java.util.SortedMap;

public class LoanDTO {

    public class PaymentDTO{

        private final int originalYazToPay;
        private int actualPaymentYaz;
        private double loanPayment;
        private double interestPayment;
        private boolean isPaid;


        public PaymentDTO(int originalYaz, double loanPayment, double interestPayment) {
            this.originalYazToPay = originalYaz;
            this.loanPayment = loanPayment;
            this.interestPayment = interestPayment;

        }

        public int getOriginalYazToPay() { return originalYazToPay; }
        public int getActualPaymentYaz() { return actualPaymentYaz; }
        public double getLoanPayment() { return loanPayment; }
        public double getInterestPayment() { return interestPayment; }
        public boolean isPaid() { return isPaid; }

        @Override
        public String toString() {
            return ("Payment yaz: " + actualPaymentYaz + "\n" +
                    "Loan payment: " + loanPayment + "\n" +
                    "Interest payment: " + interestPayment + "\n" +
                    "Total payment: " + (loanPayment + interestPayment) + "\n");
        }
    }

    public enum LoanStatus {
        NEW, PENDING, ACTIVE, IN_RISK, FINISHED
    }

    public static class LenderDetailsDTO {

        public String lenderName;
        public double lendersInvestAmount;

        public LenderDetailsDTO(String lender, double amount)
        {
            lenderName = lender;
            lendersInvestAmount = amount;
        }
    };

    private final int loanId;
    private final String customerName;
    private final double initialAmount;
    private final int maxYazToPay;
    private final double interestPerPayment;
    private final double totalInterest;
    private final int yazPerPayment;
    private final String category;
    private Loan.LoanStatus status;
    private double debt;
    private double paidInterest;
    private double paidLoan;


    private  SortedMap<Integer, PaymentDTO> unpaidPayments;
    private  SortedMap<Integer, PaymentDTO> paidPayments;


    //pending info
    private ArrayList<LenderDetailsDTO> lendersNameAndAmount;
    private double totalMoneyRaised;

    //active info
    private int activationYaz;
    private int nextPaymentYaz;

    //finish info
    private int finishYaz;


    public LoanDTO(int id, String custName, double initialAmount, int totalYaz, double interestPerPayment, double totalInterest, int yazPerPayment,
                   Loan.LoanStatus status, String category)
    {
        loanId = id;
        customerName = custName;
        this.initialAmount = initialAmount;
        this.maxYazToPay = totalYaz;
        this.interestPerPayment = interestPerPayment;
        this.totalInterest = totalInterest;
        this.yazPerPayment = yazPerPayment;
        this.category = category;
        this.status = status;
        /*this.lendersNameAndAmount = lendersAndAmounts;
        this.debt = debt;
        this.paidInterest = paidInterest;
        this.paidLoan = paidLoan;*/
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
    public String getCategory() {
        return category;
    }
    public Loan.LoanStatus getStatus() {
        return status;
    }
    public ArrayList<LenderDetailsDTO> getLendersNamesAndAmounts() {
        return lendersNameAndAmount;
    }
    public double getTotalMoneyRaised() {
        return totalMoneyRaised;
    }
    public int getActivationYaz() {
        return activationYaz;
    }
    public int getNextPaymentYaz() {
        return nextPaymentYaz;
    }
    public SortedMap<Integer, PaymentDTO> getUnpaidPayments() { return unpaidPayments; };
    public SortedMap<Integer, PaymentDTO> getPaidPayments() { return paidPayments; }
    public double getDebt() { return debt; }
    public int getFinishYaz() { return finishYaz; }


    //setters
    public void setStatus(Loan.LoanStatus status) {
        this.status = status;
    }
    public void setTotalMoneyRaised(double totalMoneyRaised) {
        this.totalMoneyRaised = totalMoneyRaised;
    }
    public void setActivationYaz(int activationYaz) {
        this.activationYaz = activationYaz;
    }
    public void setNextPaymentYaz(int nextPaymentYaz) {
        this.nextPaymentYaz = nextPaymentYaz;
    }
    public void setFinishYaz(int finishYaz) { this.finishYaz = finishYaz; }
    public void addToLendersNameAndAmount(String lender, double amount)
    {
        LenderDetailsDTO ld = new LenderDetailsDTO(lender, amount);
        lendersNameAndAmount.add(ld);
    }

    public void addPayment(int originalYaz, double originalLoan, double originalInterest) {
        LoanDTO.PaymentDTO p = new LoanDTO.PaymentDTO(originalYaz, originalLoan, originalInterest);
        unpaidPayments.put(p.originalYazToPay, p);
    }

    public int findNextPaymentYaz() {
        return unpaidPayments.get(unpaidPayments.firstKey()).getOriginalYazToPay();
    }

    public double findPaymentTotalAmount(PaymentDTO p) {

        return (p.loanPayment + p.interestPayment);
    }

    public double findLoanAndInterestTotalAmount() {

        double sum=0;

        for(PaymentDTO p : unpaidPayments.values())
        {
            sum += findPaymentTotalAmount(p);
        }

        for(PaymentDTO p : paidPayments.values())
        {
            sum += findPaymentTotalAmount(p);
        }

        return sum;
    }

    public void setUnpaidPayments(Engine.PaymentsDB.PaymentsDB payments)
    {

        for(LoanPaymentsData.Payment p : payments.getPayments().values())
        {
            PaymentDTO payment = new PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
                    p.getInterestPartOfThePayment());

            unpaidPayments.put(payment.actualPaymentYaz, payment);
        }

    }
    public void setPaidPayments(Engine.PaymentsDB.PaymentsDB payments)
    {
        for(LoanPaymentsData.Payment p : payments.getPayments().values())
        {
            PaymentDTO payment = new PaymentDTO(p.getScheduledYaz(), p.getLoanPartOfThePayment(),
                    p.getInterestPartOfThePayment());

            paidPayments.put(payment.actualPaymentYaz, payment);
        }
    }


    @Override
    public String toString() {

        String toReturn = new String();
        toReturn += LoanBasicInfoToString();

        switch(status)
        {
            case PENDING:
            {
                for(LenderDetailsDTO ld : lendersNameAndAmount)
                {
                    toReturn += ("Name: " + ld.lenderName + " Amount invested: " + ld.lendersInvestAmount);
                }
                double amountMissing = initialAmount -  totalMoneyRaised;
                toReturn += ("The amount raised so far is: " + totalMoneyRaised +
                        "\n Loan needs " + amountMissing + " more to become active\n");
                break;
            }
            case ACTIVE:
            {
                toReturn +=("Registered lenders for loan:\n");

                for(LenderDetailsDTO ld : lendersNameAndAmount)
                {
                    toReturn += ("Name: " + ld.lenderName + " Amount invested: " + ld.lendersInvestAmount + "\n");
                }

                toReturn += ("The loan became active in yaz number " + activationYaz +
                        "\nNext payment in yaz number " + findNextPaymentYaz() + "\n" +
                        "All payments made so far:\n");

                int i=1;
                for (PaymentDTO p : paidPayments.values())
                {
                    toReturn += i +". " + p.toString();
                    i++;
                }

                toReturn+= ("Total loan paid: " + paidLoan + " , remained to pay: " + (initialAmount-paidLoan) +
                        "\nTotal interest paid: " + paidInterest + " , remained to pay: " + (totalInterest-paidInterest) + "\n");
                break;
            }
            case IN_RISK:
            {
                toReturn +=("Registered lenders for loan:\n");

                for(LenderDetailsDTO ld : lendersNameAndAmount)
                {
                    toReturn += ("Name: " + ld.lenderName + " Amount invested: " + ld.lendersInvestAmount + "\n");
                }

                toReturn += ("The loan became active in yaz number " + activationYaz +
                        "\nNext payment in yaz number " + findNextPaymentYaz() + "\n" +
                        "All payments made so far:\n");

                int i=1;
                for (PaymentDTO p : paidPayments.values())
                {
                    toReturn += i +". " + p.toString();
                    i++;
                }

                toReturn+= ("Total loan paid: " + paidLoan + " , remained to pay: " + (initialAmount-paidLoan) +
                        "\nTotal interest paid: " + paidInterest + " , remained to pay: " + (totalInterest-paidInterest) + "\n");
                    toReturn+=lendersNameAndAmountToString();
                    //show unpaid payments
                    break;
            }
            case FINISHED:
            {
                toReturn += lendersNameAndAmountToString();
                toReturn+=("Start yaz: " + activationYaz + "\nFinish yaz: " +finishYaz);

                toReturn+=("All payments:\n");

                int i=1;
                for (PaymentDTO p : paidPayments.values())
                {
                    toReturn += i +". " + p.toString();
                    i++;
                }

                break;
            }
        }
        return toReturn;
    }

    public String LoanBasicInfoToString()
    {
        String toReturn = new String();

        toReturn += "Loan id: " + loanId + "\n" +
                "Loan category: " + category + "\n" +
                "Original loan amount: " + initialAmount + "\n" +
                "Total time to repay the loan: " + maxYazToPay +"\n" +
                "Payment will be made every " + yazPerPayment + " yaz\n" +
                "Interest per payment: " + interestPerPayment + "%\n" +
                "Total loan amount (including interest): " +
                findLoanAndInterestTotalAmount() + "\n" +
                "The loan is in status: " + status + "\n";

        return toReturn;
    }

    public String lendersNameAndAmountToString()
    {
        String toReturn = new String();

        for(LenderDetailsDTO ld : lendersNameAndAmount)
        {
            toReturn += ("Name: " + ld.lenderName + " Amount invested: " + ld.lendersInvestAmount + "\n");
        }

        return toReturn;
    }
}