package Engine.PaymentsDB;

import Engine.LoanPaymentsData;

import java.util.SortedMap;
import java.util.TreeMap;

public class PayedPaymentsByYaz implements PaymentsDB {
    private final LoanPaymentsData.Payment.PaymentType dbPaymentsType;
    private SortedMap<Integer, LoanPaymentsData.Payment> db;

    public PayedPaymentsByYaz(){
        dbPaymentsType = LoanPaymentsData.Payment.PaymentType.PAYED;
        db = new TreeMap<Integer, LoanPaymentsData.Payment>();
    }

    @Override
    public void addNewPayment(LoanPaymentsData.Payment p) {
        Integer paymentCompletionYaz = p.getActualPaymentYaz();
        if(db.containsKey(paymentCompletionYaz)){
            throw new IllegalAccessException("There was a problem while adding new payment to 'UnpayedPaymentsByYaz' - there is already a payment for this yaz");
        }

        db.put(paymentCompletionYaz, p);
    }

    @Override
    public void clearAll(){
        db.clear();
    }

    @Override
    public void removePaymentForSpecificYaz(int yaz){
        if(!db.containsKey(yaz)){
            throw new IllegalAccessException("There was a problem while removing payment to 'UnpayedPaymentsByYaz' - there is no payment in DB for this yaz");
        }

        db.remove(yaz);
    }

    @Override
    public LoanPaymentsData.Payment.PaymentType getPaymentType(){
        return this.dbPaymentsType;
    }
}
