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
        private double InterestPartOfThePayment;
        private String borrowerName;

        public Payment(int ScheduledYaz, double loanPartOfThePayment, double interestPartOfThePayment, String borrowerName, PaymentType paymentType) {
            this.scheduledYaz = -1;
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
        public double getBothPartsOfAmountToPay(){return this.loanPartOfThePayment + this.InterestPartOfThePayment;}

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
        public void setScheduledYaz(int yaz){this.scheduledYaz = yaz;}

        public int compare(LoanPaymentsData.Payment p1, LoanPaymentsData.Payment p2) { return (p1.scheduledYaz - p2.scheduledYaz); }
    }

    private final Engine.Loan containingLoan;
    private final LinkedList<PaymentsDB> paymentsDataBases;

    public LoanPaymentsData(Engine.Loan containingLoan){
        this.containingLoan = containingLoan;
        this.paymentsDataBases = new LinkedList<PaymentsDB>();
        this.initPaymentDataBases();
    }

    private void initPaymentDataBases(){
        paymentsDataBases.add(new UnpayedPaymentsByYaz());
        paymentsDataBases.add(new PayedPaymentsByYaz());
        paymentsDataBases.add(new ExpiredPaymentsByYaz());
    }

    private void addNewPaymentToDataBase(Payment p){
        try{
            for (PaymentsDB db: paymentsDataBases) {
                if(db.getPaymentType() == p.paymentType){
                    db.addNewPayment(p);
                }
            }
        } catch (Exception e){
            throw new DataBaseAccessException(paymentsDataBases, "There was a problem when adding new payment to Data Bases");
        }




    }

    private Payment createNewUnpaidPayment(int scheduledYazOfPayment, double loanPartReturnedByBorrowerEveryPaymentTime, double interestPartReturnedByBorrowerEveryPaymentTime, String borrowerName){
        Payment p = new Payment(scheduledYazOfPayment, loanPartReturnedByBorrowerEveryPaymentTime, interestPartReturnedByBorrowerEveryPaymentTime, borrowerName, LoanPaymentsData.PaymentType.UNPAID);

        return p;
    }

    public void createAllLoanPayments(){
        int numberOfPayments = containingLoan.getMaxYazToPay() / containingLoan.getPaymentRateInYaz();
        double loanPartOfEachPayment = containingLoan.getInitialAmount() / numberOfPayments;
        double interestPartOfEachPayment = (containingLoan.getInterestPerPaymentSetByBorrowerInPercents() / 100) * loanPartOfEachPayment;
        double finalAmountOfEachPayment = loanPartOfEachPayment + interestPartOfEachPayment;
        int yazToSetForPayment = 0;
        int loanPaymentRate = containingLoan.getPaymentRateInYaz();

        for (int i = 0; i < numberOfPayments; i++) {
            Payment p = createNewUnpaidPayment(yazToSetForPayment, loanPartOfEachPayment, interestPartOfEachPayment, containingLoan.getBorrowerName());
            addNewPaymentToDataBase(p);
            yazToSetForPayment += loanPaymentRate;
        }
    }

    public void addNewPayment(Payment newPayment){

        for (PaymentsDB db:this.paymentsDataBases){
            if(db.getPaymentType() == newPayment.paymentType){
                try {
                    db.addNewPayment(newPayment);
                } catch (Exception e){
                    throw new DataBaseAccessException(db, "There was a problem when adding");
                }
            }
        }
    }

    public LoanPaymentsData.Payment peekPaymentForYaz(int yaz){
        LoanPaymentsData.Payment payment = null;

        for (PaymentsDB singleDataBase: paymentsDataBases){
            try {
                payment = singleDataBase.peekPaymentByYaz(yaz);
            } catch (Exception e){
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

        for (PaymentsDB singleDataBase: paymentsDataBases){
            try {
                payment = singleDataBase.pollPaymentByYaz(yaz);
            } catch (Exception e){
                throw new DataBaseAccessException(singleDataBase, "There was a problem while polling payment from data base");
            }

            if(payment != null){
                return payment;
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while polling payment from payments DB - null value returned - no payment found in DB");
    }

    public Object getPayments(PaymentType type){
        for (PaymentsDB db: paymentsDataBases){
            if(db.getPaymentType() == type){
                return db;
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while trying to get payments from type: " + type + " from data bases, no such type was found in data bases");
    }

    public boolean isTherePaymentsFromSpecificType(PaymentType type){
        for (PaymentsDB db: paymentsDataBases){
            if(db.getPaymentType() == type){
                return !db.isEmpty();
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while trying to check if isTherePaymentsFromSpecificType: " + type + " from data bases, no such type was found in data bases");
    }

    public Payment getEarliestExpiredPayment(){
        for (PaymentsDB db: paymentsDataBases){
            if(db.getPaymentType() == PaymentType.EXPIRED){
                return db.getEarliestPayment();
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while trying to getEarliestExpiredPayment from data bases, no such type was found in data bases");
    }

    public Payment getEarliestUnpaidPayment(){
        for (PaymentsDB db: paymentsDataBases){
            if(db.getPaymentType() == PaymentType.UNPAID){
                return db.getEarliestPayment();
            }
        }

        throw new DataBaseAccessException(paymentsDataBases, "There was a problem while trying to getEarliestUnpaidPayment from data bases, no such type was found in data bases");
    }

    public void addYazToAllPayments(int yazToAdd){
        for (PaymentsDB db :paymentsDataBases){
            Map<Integer, Payment> actualDataBase = (Map<Integer, Payment>)db.getActualData();

            for (Map.Entry<Integer, Payment> set: actualDataBase.entrySet()){
                Payment p = set.getValue();

                p.setScheduledYaz(p.getScheduledYaz() + yazToAdd);
            }
        }
    }

}
