package Engine;

import java.util.*;
import Engine.PaymentsDB.*;

public class LoanPaymentsData {

    public enum PaymentType{
        UNPAID,
        PAID,
        EXPIRED
    }
    public class Payment {

        private PaymentType paymentType;
        private final int scheduledYaz;
        private int actualPaymentYaz;
        private double loanPartOfThePayment;
        private double InterestPartOfThePayment;
        private String borrowerName;

        public Payment(int ScheduledYaz, double loanPartOfThePayment, double interestPartOfThePayment, String borrowerName, PaymentType paymentType) {
            this.scheduledYaz = ScheduledYaz;
            this.loanPartOfThePayment = loanPartOfThePayment;
            this.InterestPartOfThePayment = interestPartOfThePayment;
            this.borrowerName = borrowerName;
            this.paymentType = paymentType;
        }

        public int getActualPaymentYaz() {
            return actualPaymentYaz;
        }
        public int getScheduledYaz(){return scheduledYaz;}
        public double getLoanPartOfThePayment() {
            return loanPartOfThePayment;
        }
        public double getInterestPartOfThePayment() {
            return InterestPartOfThePayment;
        }
        public boolean isPaid() {
            return this.paymentType == PaymentType.PAID;
        }
        public String getBorrowerName(){return this.borrowerName;}
        public PaymentType getPaymentType(){return this.paymentType;}

        public void setActualPaymentYaz(int actualPaymentYaz) {
            this.actualPaymentYaz = actualPaymentYaz;
        }
        public void setLoanPartOfThePayment(double loanPartOfThePayment) {
            this.loanPartOfThePayment = loanPartOfThePayment;
        }
        public void setInterestPartOfThePayment(double interestPartOfThePayment) {
            this.InterestPartOfThePayment = interestPartOfThePayment;
        }
        public void setPaid(boolean payed) {
            this.paymentType = PaymentType.PAID;
        }
        public void setPaymentType(PaymentType newType){this.paymentType = newType;}

        public int compare(LoanPaymentsData.Payment p1, LoanPaymentsData.Payment p2) { return (p1.scheduledYaz - p2.scheduledYaz); }
    }

    private final Engine.Loan containingLoan;
    private final LinkedList<PaymentsDB> paymentsDataBase;

    public LoanPaymentsData(Engine.Loan containingLoan){
        this.containingLoan = containingLoan;
        this.paymentsDataBase = new LinkedList<PaymentsDB>();
        this.initPaymentDataBases();
    }

    private void initPaymentDataBases(){
        paymentsDataBase.add(new UnpayedPaymentsByYaz());
        paymentsDataBase.add(new PayedPaymentsByYaz());
        paymentsDataBase.add(new ExpiredPaymentsByYaz());
    }

    private void addNewPaymentToDataBase(Payment p){
        for (PaymentsDB db:paymentsDataBase) {
            if(db.getPaymentType() == p.paymentType){
                db.addNewPayment(p);
            }
        }
    }

    private Payment createNewUnpaidPayment(int scheduledYazOfPayment, double loanPartReturnedByBorrowerEveryPaymentTime, double interestPartReturnedByBorrowerEveryPaymentTime, String borrowerName){
        Payment p = new Payment(scheduledYazOfPayment, loanPartReturnedByBorrowerEveryPaymentTime, interestPartReturnedByBorrowerEveryPaymentTime, borrowerName, Payment.PaymentType.UNPAID);

        return p;
    }

    public void createAllLoanPayments(){
        int numberOfPayments = containingLoan.getMaxYazToPay() / containingLoan.getPaymentRateInYaz();
        double loanPartOfEachPayment = containingLoan.getInitialAmount() / numberOfPayments;
        double interestPartOfEachPayment = (containingLoan.getInterestPerPaymentSetByBorrowerInPercents() / 100) * loanPartOfEachPayment;
        double finalAmountOfEachPayment = loanPartOfEachPayment + interestPartOfEachPayment;
        int yazToSetForPayment = containingLoan.getActivationYaz();

        for (int i = 0; i < numberOfPayments; i++) {
            Payment p = createNewUnpaidPayment(yazToSetForPayment, loanPartOfEachPayment, interestPartOfEachPayment, containingLoan.getBorrowerName());
            addNewPaymentToDataBase(p);
        }
    }
}
