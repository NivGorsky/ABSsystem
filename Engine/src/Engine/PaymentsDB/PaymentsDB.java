package Engine.PaymentsDB;
import Engine.LoanPaymentsData;

import java.util.SortedMap;

public interface PaymentsDB {
    public void addNewPayment(LoanPaymentsData.Payment p);
    public void clearAll();
    public void removePaymentForSpecificYaz(int yaz);
    public LoanPaymentsData.PaymentType getPaymentType();
    public SortedMap<Integer, LoanPaymentsData.Payment> getPayments();
    public LoanPaymentsData.Payment peekPaymentByYaz(int yaz);
    public LoanPaymentsData.Payment pollPaymentByYaz(int yaz);
    public boolean isEmpty();
    public Object getActualData();
    public LoanPaymentsData.Payment getEarliestPayment();
}
