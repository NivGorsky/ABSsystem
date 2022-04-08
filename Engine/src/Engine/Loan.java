package Engine;

import java.util.LinkedList;

public class Loan {

    static int loansNum = 0;

    public enum LoanCategory {
        OPENING_BUSINESS, PROPERTY_RENOVATION, CLOSE_OVERDRAW, EVENT, PURCHASE_CAR, PURCHASE_HOUSE
    }

    public enum LoanStatus {
        NEW, PENDING, ACTIVE, IN_RISK, FINISHED
    }

    public class LenderDetails {
        public Customer lender;
        public double lendersAmount;
        public double lendersPartOfLoanInPercent;

    };

    //loan's general data
    private final String loanName;
    private final int loanId; //string uuid
    private final LoanCategory category;
    private final double initialAmount;
    private final String borrowerName;
    private final double interestPerPaymentSetByBorrowerInPercents;
    private final double totalInterestForLoan;
    private LoanStatus status;
    private Account account;

    //payments data
    private LoanPaymentsData paymentsData;
    private final int paymentRateInYaz;
    private double interestPaid;
    private double amountPaid;
    private double debt;

    //time-line data
    private final int maxYazToPay;
    private int activationYaz;
    private int yazRemainingToPay;
    private int finishYaz;

    //loan's lenders' data
    private LinkedList<LenderDetails> lendersBelongToLoan;
    private double loanPercentageTakenByLenders;
    private double loanAmountFinancedByLenders;

    public Loan(String loanName, String borrowerName, double originalLoanAmount, int yaz, int paymentRateInYaz, double interestPercentPerPayment, LoanCategory category)
    {
        //init loan's general data
        this.loanName = loanName;
        this.loanId = loansNum;
        loansNum++;
        this.category = category;
        this.initialAmount = originalLoanAmount;
        this.borrowerName = borrowerName;
        this.interestPerPaymentSetByBorrowerInPercents = interestPercentPerPayment;
        this.totalInterestForLoan = (1+(interestPerPaymentSetByBorrowerInPercents/100))*initialAmount;
        this.setLoanStatus(LoanStatus.NEW);
        this.account = new Account(0);

        //init loan's payments data
        this.paymentsData = new LoanPaymentsData(this);
        this.paymentRateInYaz = paymentRateInYaz;
        this.interestPaid = 0;
        this.amountPaid = 0;
        this.debt = 0;

        //init time-line data
        this.maxYazToPay = yaz;
        this.activationYaz = -1;
        this.yazRemainingToPay = yaz;
        this.finishYaz = -1;

        //init loan's lenders data
        this.lendersBelongToLoan = new LinkedList<LenderDetails>();
        this.loanPercentageTakenByLenders = 0;
        this.loanAmountFinancedByLenders = 0;
    }

    //getters
    public int getLoanId() { return loanId; }
    public String getBorrowerName() {
        return borrowerName;
    }
    public double getInitialAmount() {
        return initialAmount;
    }
    public int getMaxYazToPay() {
        return maxYazToPay;
    }
    public int getPaymentRateInYaz() {
        return paymentRateInYaz;
    }
    public double getInterestPerPaymentSetByBorrowerInPercents() {
        return interestPerPaymentSetByBorrowerInPercents;
    }
    public LoanCategory getCategory() {
        return category;
    }
    public LinkedList<LenderDetails> getLendersDetails() {
        return lendersBelongToLoan;
    }
    public int getYazRemainingToPay() {
        return yazRemainingToPay;
    }
    public double getInterestPaid() {
        return interestPaid;
    }
    public double getAmountPaid() {
        return amountPaid;
    }
    public LoanStatus getStatus() {
        return status;
    }
    public int getActivationYaz() {
        return activationYaz;
    }
    public int getFinishYaz() { return finishYaz; }
    public double getDebt() { return debt; }
    public double getTotalInterestForLoan() { return totalInterestForLoan; }
    public double getLoanPercentageTakenByLenders(){return this.loanPercentageTakenByLenders;}
    public double getLoanAmountFinancedByLenders(){return this.loanAmountFinancedByLenders;}
    public Account getLoanAccount(){return this.account;}

    //setters
    public void setLoanStatus(Loan.LoanStatus newStatus){

        this.status = newStatus;
        switch (newStatus) {
            case ACTIVE:

                break;

            case PENDING:

                break;

            case IN_RISK:

                break;

            case FINISHED:

                break;

            case NEW:
                break;
        }
    }


    //methods
    public void addNewLender(Engine.Customer newLender, double lendersPartOfLoanAmount){
        Loan.LenderDetails newLenderDetails = new Loan.LenderDetails();
        newLenderDetails.lender = newLender;
        newLenderDetails.lendersAmount = lendersPartOfLoanAmount;
        newLenderDetails.lendersPartOfLoanInPercent = lendersPartOfLoanAmount / this.initialAmount;
        this.lendersBelongToLoan.add(newLenderDetails);
        this.loanPercentageTakenByLenders += newLenderDetails.lendersPartOfLoanInPercent;
        this.loanAmountFinancedByLenders += newLenderDetails.lendersAmount;
    }

    public boolean isLoanReadyToBeActive(){
        boolean result = false;

        if (this.loanAmountFinancedByLenders == this.initialAmount && this.loanPercentageTakenByLenders == 100){
            result = true;
        }

        return result;
    }

    public LoanPaymentsData.Payment peekPaymentForSpecificYaz(int yaz){
        return paymentsData.peekPaymentForYaz(yaz);
    }

    public LoanPaymentsData.Payment pollPaymentForSpecificYaz(int yaz){
        return paymentsData.pollPaymentForYaz(yaz);
    }

    public void addNewPayment(LoanPaymentsData.Payment newPayment){
        paymentsData.addNewPayment(newPayment);
    }

    public Object getPayments(LoanPaymentsData.PaymentType paymentType){
        return paymentsData.getPayments(paymentType);
    }

    public boolean isTherePaymentsOfSpecificType(LoanPaymentsData.PaymentType type){
        return paymentsData.isTherePaymentsFromSpecificType(type);
    }
}



