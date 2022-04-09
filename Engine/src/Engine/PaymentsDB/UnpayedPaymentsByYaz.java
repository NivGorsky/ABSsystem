package Engine.PaymentsDB;
import Engine.LoanPaymentsData;
import java.util.SortedMap;
import java.util.TreeMap;

public class UnpayedPaymentsByYaz implements PaymentsDB {
    private final LoanPaymentsData.PaymentType dbPaymentsType;
    private SortedMap<Integer, LoanPaymentsData.Payment> db;

    public UnpayedPaymentsByYaz(){
        dbPaymentsType = LoanPaymentsData.PaymentType.UNPAID;
        db = new TreeMap<Integer, LoanPaymentsData.Payment>();
    }

    @Override
    public SortedMap<Integer, LoanPaymentsData.Payment> getPayments()
    {
        return db;
    }

    @Override
    public void addNewPayment(LoanPaymentsData.Payment p) {
        Integer scheduledYazOfNewPayment = p.getScheduledYaz();
        if(db.containsKey(scheduledYazOfNewPayment)){
//            throw new IllegalAccessException("There was a problem while adding new payment to 'UnpayedPaymentsByYaz' - there is already a payment for this yaz");
        }

        db.put(scheduledYazOfNewPayment, p);
    }

    @Override
    public void clearAll(){
        db.clear();
    }

    @Override
    public void removePaymentForSpecificYaz(int yaz){
        if(!db.containsKey(yaz)){
//            throw new IllegalAccessException("There was a problem while removing payment to 'UnpayedPaymentsByYaz' - there is no payment in DB for this yaz");
        }

        db.remove(yaz);
    }

    @Override
    public LoanPaymentsData.PaymentType getPaymentType(){
        return this.dbPaymentsType;
    }

    @Override
    public LoanPaymentsData.Payment peekPaymentByYaz(int yaz){
        LoanPaymentsData.Payment result = null;

        if(db.containsKey(yaz)){
            result = db.get(yaz);
        }

        return result;
    }

    @Override
    public LoanPaymentsData.Payment pollPaymentByYaz(int yaz){
       LoanPaymentsData.Payment value = db.get(yaz);

       if(value != null){
           db.remove(yaz);
       }

       return value;
    }

    @Override
    public boolean isEmpty(){return db.isEmpty();}

    @Override
    public Object getActualData(){return this.db;}

    @Override
    public LoanPaymentsData.Payment getEarliestPayment(){
        if(!db.isEmpty()){
            Integer key = db.firstKey();

            return db.get(key);
        }

        return null;
    }
}

