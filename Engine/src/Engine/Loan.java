package Engine;

import java.util.LinkedList;
import Exceptions.*;

public class Loan
{
    static int loansNum = 0;

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
    private final String category;
    private final int initialAmount;
    private final String borrowerName;
    private final int interestPerPaymentSetByBorrowerInPercents;
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

    public Loan(String loanName, String borrowerName, int originalLoanAmount, int yaz, int paymentRateInYaz,
                int interestPercentPerPayment, String category, int currentYaz)
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
        this.setLoanStatus(LoanStatus.NEW, currentYaz);
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
    public String getCategory() {
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
    public LoanPaymentsData getPaymentsData() { return paymentsData; }
    public LinkedList<LenderDetails> getLendersBelongToLoan() { return lendersBelongToLoan; }
    public double getLoanPercentageTakenByLenders(){return this.loanPercentageTakenByLenders;}
    public double getLoanAmountFinancedByLenders(){return this.loanAmountFinancedByLenders;}
    public Account getLoanAccount(){return this.account;}

    //setters
    public void setLoanStatus(Loan.LoanStatus newStatus, int currentYaz){

        this.status = newStatus;
        switch (newStatus) {
            case ACTIVE:
                updateLoanToActive(currentYaz);

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
    public void addNewLender(Engine.Customer newLender, double lendersPartOfLoanAmount) throws Exception
    {
        double amountOpenToLend = this.getInitialAmount() - this.getLoanAmountFinancedByLenders();

        if(newLender.getName().equals(this.getBorrowerName())){
            throw new SystemRestrictionsException(this, "User is not allowed to register to a loan as lender when the user is already registered as borrower");
        }

        if(lendersPartOfLoanAmount > amountOpenToLend){
            throw new ValueOutOfRangeException(1, amountOpenToLend, "User part of loan can't be greater than loan's open amount for lending");
        }

        for(LenderDetails lender : this.lendersBelongToLoan)
        {
            if(newLender.getName() == lender.lender.getName())
            {
                lender.lendersAmount += lendersPartOfLoanAmount;
                lender.lendersPartOfLoanInPercent = (lendersPartOfLoanAmount/initialAmount)*100;
                return;
            }
        }

        Loan.LenderDetails newLenderDetails = new Loan.LenderDetails();
        newLenderDetails.lender = newLender;
        newLenderDetails.lendersAmount = lendersPartOfLoanAmount;
        newLenderDetails.lendersPartOfLoanInPercent = lendersPartOfLoanAmount / this.initialAmount;
        this.lendersBelongToLoan.add(newLenderDetails);
        this.loanPercentageTakenByLenders += newLenderDetails.lendersPartOfLoanInPercent;this.loanAmountFinancedByLenders += newLenderDetails.lendersAmount;
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
        paymentsData.addNewPaymentToDataBase(newPayment);
    }

    public Object getPayments(LoanPaymentsData.PaymentType paymentType){
        return paymentsData.getPayments(paymentType);
    }

    public boolean isTherePaymentsOfSpecificType(LoanPaymentsData.PaymentType type){
        return paymentsData.isTherePaymentsFromSpecificType(type);
    }

    public LoanPaymentsData.Payment getEarliestUnpaidOrExpiredPayment(){
        LoanPaymentsData.Payment earliestExpiredPayment = paymentsData.getEarliestExpiredPayment();
        LoanPaymentsData.Payment earliestUnpaidPayment = paymentsData.getEarliestUnpaidPayment();

        if(earliestUnpaidPayment == null && earliestExpiredPayment == null){
            throw new DataBaseAccessException(paymentsData, "There was a problem while trying to get earliest payment - there are on unpaid or expired payments - loan shold be finished");
        }

        else if(earliestUnpaidPayment == null){
            return earliestExpiredPayment;
        }

        else if(earliestExpiredPayment == null){
            return earliestUnpaidPayment;
        }

        else{
            double earliestUnpaidAmount = earliestUnpaidPayment.getBothPartsOfAmountToPay();
            double earliestExpiredAmount = earliestExpiredPayment.getBothPartsOfAmountToPay();

            if(earliestUnpaidAmount >= earliestExpiredAmount){
                return earliestUnpaidPayment;
            }

            else{
                return earliestExpiredPayment;
            }
        }
    }

    private void updateLoanToActive(int yaz){
        this.paymentsData.addYazToAllPayments(yaz);
    }
}



