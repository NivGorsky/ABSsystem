package Engine;

import java.util.*;
import Engine.PaymentsDB.*;
import Exceptions.DataBaseAccessException;

public class LoanPaymentsData {

    public enum PaymentType{
        UNPAID,
        PAID,
        EXPIRED
    }
    public class Payment {

        private PaymentType paymentType;
        private int scheduledYaz;
        private int actualPaymentYaz;
        private double loanPartOfThePayment;
        private double loanPartThatWasPaid;
        private double interestPartThatWasPaid;
        private double interestPartOfThePayment;
        private String borrowerName;

        public Payment(int scheduledYaz, double loanPartOfThePayment, double interestPartOfThePayment, String borrowerName, PaymentType paymentType) {
            this.scheduledYaz = scheduledYaz;
            this.loanPartOfThePayment = loanPartOfThePayment;
            this.interestPartOfThePayment = interestPartOfThePayment;
            this.borrowerName = borrowerName;
            this.paymentType = paymentType;
            this.loanPartThatWasPaid = 0;
            this.interestPartThatWasPaid = 0;
        }

        public int getActualPaymentYaz() {
            return actualPaymentYaz;
        }
        public int getScheduledYaz(){return scheduledYaz;}
        public double getLoanPartOfThePayment() {
            return loanPartOfThePayment;
        }
        public double getInterestPartOfThePayment() {return interestPartOfThePayment;}
        public double getInterestPartThatWasPaid() { return interestPartThatWasPaid; }
        public boolean isPaid() {
            return this.paymentType == PaymentType.PAID;
        }
        public String getBorrowerName(){return this.borrowerName;}
        public PaymentType getPaymentType(){return this.paymentType;}
        public double getBothPartsOfAmountToPay(){return this.loanPartOfThePayment + this.interestPartOfThePayment;}

        public void setActualPaymentYaz(int actualPaymentYaz) {
            this.actualPaymentYaz = actualPaymentYaz;
        }
        public void setLoanPartOfThePayment(double loanPartOfThePayment) {
            this.loanPartOfThePayment = loanPartOfThePayment;
        }
        public void setInterestPartOfThePayment(double interestPartOfThePayment) {
            this.interestPartOfThePayment = interestPartOfThePayment;
        }
        public void setPaid(boolean payed) {
            this.paymentType = PaymentType.PAID;
        }
        public void setPaymentType(PaymentType newType){this.paymentType = newType;}
        public void setScheduledYaz(int yaz){this.scheduledYaz = yaz;}
        public void setPartialPaidInPercents(double percentageOfPaymentThatWasPaid){
            double loanPartThatWasPaidInAmount = (percentageOfPaymentThatWasPaid/ 100) * loanPartOfThePayment;
            double interestPartThatWasPaidInAmount = (percentageOfPaymentThatWasPaid / 100) * interestPartOfThePayment;
            this.loanPartThatWasPaid += loanPartThatWasPaidInAmount;
            this.interestPartThatWasPaid += interestPartThatWasPaidInAmount;
        }

        public double getBothPartsOfPaymentThatWasPaid(){return this.loanPartThatWasPaid + this.interestPartThatWasPaid;}

        public int compare(LoanPaymentsData.Payment p1, LoanPaymentsData.Payment p2) { return (p1.scheduledYaz - p2.scheduledYaz); }
    }

//    private final Engine.Loan containingLoan;

    private final Map<LoanPaymentsData.PaymentType, PaymentsDB> paymentsDataBases;

    public LoanPaymentsData(){
//        this.containingLoan = containingLoan;
        this.paymentsDataBases = new TreeMap<LoanPaymentsData.PaymentType, PaymentsDB>();
        this.initPaymentDataBases();
    }

    private void initPaymentDataBases()
    {
        paymentsDataBases.put(PaymentType.UNPAID, new UnpaidPaymentsByYaz());
        paymentsDataBases.put(PaymentType.PAID, new PayedPaymentsByYaz());
        paymentsDataBases.put(PaymentType.EXPIRED, new ExpiredPaymentsByYaz());
    }

    public void addNewPaymentToDataBase(Payment p)
    {
        paymentsDataBases.get(p.paymentType).addNewPayment(p);
    }

    private Payment createNewUnpaidPayment(int scheduledYazOfPayment, double loanPartReturnedByBorrowerEveryPaymentTime, double interestPartReturnedByBorrowerEveryPaymentTime, String borrowerName)
    {
        Payment p = new Payment(scheduledYazOfPayment, loanPartReturnedByBorrowerEveryPaymentTime, interestPartReturnedByBorrowerEveryPaymentTime, borrowerName, PaymentType.UNPAID);
        return p;
    }

    public void createAllLoanPayments(int maxYazToPay, int paymentRateInYaz, int initialAmount, double interestPerPaymentSetByBorrowerInPercents, String borrowerName){
        int numberOfPayments = maxYazToPay / paymentRateInYaz;
        double loanPartOfEachPayment = initialAmount / numberOfPayments;
        double interestPartOfEachPayment = (interestPerPaymentSetByBorrowerInPercents / 100) * loanPartOfEachPayment;
        double finalAmountOfEachPayment = loanPartOfEachPayment + interestPartOfEachPayment;
        int yazToSetForPayment = 0;
        int loanPaymentRate = paymentRateInYaz;

        for (int i = 0; i < numberOfPayments; i++) {
            Payment p = createNewUnpaidPayment(yazToSetForPayment, loanPartOfEachPayment, interestPartOfEachPayment, borrowerName);
            addNewPaymentToDataBase(p);
            yazToSetForPayment += loanPaymentRate;
        }
    }

    public LoanPaymentsData.Payment peekPaymentForYaz(int yaz)
    {
        LoanPaymentsData.Payment payment = null;

        for (PaymentsDB singleDataBase: paymentsDataBases.values())
        {
            try {
                payment = singleDataBase.peekPaymentByYaz(yaz);
            }
            catch (Exception e){
                throw new DataBaseAccessException(singleDataBase, "There was a problem while peeking for payment in data bases");
            }

            if(payment != null){
                return payment;
            }
        }

        return null;
    }

    public LoanPaymentsData.Payment pollPaymentForYaz (int yaz){
        LoanPaymentsData.Payment payment = null;

        for (PaymentsDB singleDataBase: paymentsDataBases.values())
        {
            try {
                payment = singleDataBase.pollPaymentByYaz(yaz);
            }
            catch (Exception e){
                throw new DataBaseAccessException(singleDataBase, "There was a problem while polling payment from data base");
            }

            if(payment != null){
                return payment;
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while polling payment from payments DB - null value returned - no payment found in DB");
    }

    public Object getPayments(PaymentType type)
    {
        for (PaymentsDB db: paymentsDataBases.values())
        {
            if(db.getPaymentType() == type){
                return db;
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while trying to get payments from type: " + type + " from data bases, no such type was found in data bases");
    }

    public boolean isTherePaymentsFromSpecificType(PaymentType type)
    {
        for (PaymentsDB db: paymentsDataBases.values())
        {
            if(db.getPaymentType() == type){
                return !db.isEmpty();
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while trying to check if isTherePaymentsFromSpecificType: " + type + " from data bases, no such type was found in data bases");
    }

    public Payment getEarliestExpiredPayment()
    {
        for (PaymentsDB db: paymentsDataBases.values())
        {
            if(db.getPaymentType() == PaymentType.EXPIRED)
            {
                return db.getEarliestPayment();
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while trying to getEarliestExpiredPayment from data bases, no such type was found in data bases");
    }

    public Map<LoanPaymentsData.PaymentType, PaymentsDB> getPaymentsDataBases() { return paymentsDataBases; }

    public Payment getEarliestUnpaidPayment()
    {
        for (PaymentsDB db: paymentsDataBases.values())
        {
            if(db.getPaymentType() == PaymentType.UNPAID){
                return db.getEarliestPayment();
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while trying to getEarliestUnpaidPayment from data bases, no such type was found in data bases");
    }

    public void addYazToAllPayments(int yazToAdd)
    {
        for (PaymentsDB db :paymentsDataBases.values())
        {
            Map<Integer, Payment> actualDataBase = (Map<Integer, Payment>)db.getActualData();
            LinkedList<Payment> payments = new LinkedList<Payment>();

            for (Map.Entry<Integer, Payment> set: actualDataBase.entrySet()){
                payments.add(set.getValue());
            }
            actualDataBase.clear();

            for (Payment p:payments){
                p.setScheduledYaz(p.getScheduledYaz() + yazToAdd);
                actualDataBase.put(p.scheduledYaz, p);
            }
            payments.clear();
        }
    }
}
