package Engine.PaymentsDB;
import Engine.LoanPaymentsData;

import java.util.SortedMap;

public interface PaymentsDB {
    public void addNewPayment(LoanPaymentsData.Payment p);
    public void clearAll();
    public void removePaymentForSpecificYaz(int yaz);
    public LoanPaymentsData.PaymentType getPaymentType();
    public SortedMap<Integer, LoanPaymentsData.Payment> getPayments();
}
