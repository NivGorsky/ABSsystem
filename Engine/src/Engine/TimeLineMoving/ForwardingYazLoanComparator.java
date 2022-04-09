package Engine.TimeLineMoving;

import Engine.Loan;
import Engine.LoanPaymentsData;

import java.util.Comparator;

public class ForwardingYazLoanComparator implements Comparator<Loan> {

    @Override
    public int compare(Loan l1, Loan l2) {

        if(l1.getActivationYaz() == l2.getActivationYaz()){
            LoanPaymentsData.Payment p1 = l1.getEarliestUnpaidOrExpiredPayment();
            LoanPaymentsData.Payment p2 = l2.getEarliestUnpaidOrExpiredPayment();

            return (int)p1.getBothPartsOfAmountToPay() - (int)p2.getBothPartsOfAmountToPay();
        }

        else{
            return l1.getActivationYaz() - l2.getActivationYaz();
        }
    }
}
