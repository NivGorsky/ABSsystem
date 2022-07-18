package Engine.XML_Handler;

import Engine.Customer;
import Engine.Loan;
import Engine.LoanPaymentsData;

public class JAXBConvertor {

    public static Loan convertLoan(AbsLoan generatedLoan, int currentYaz, String customer)
    {
        Loan loan = new Loan(generatedLoan.id.trim(), customer, generatedLoan.absCapital, generatedLoan.absTotalYazTime,
                generatedLoan.absPaysEveryYaz, generatedLoan.absIntristPerPayment, generatedLoan.absCategory.trim(), currentYaz);

        LoanPaymentsData loanPayments = loan.getPaymentsData();
        loanPayments.createAllLoanPayments(loan.getMaxYazToPay(), loan.getPaymentRateInYaz(), loan.getInitialAmount(), loan.getInterestPerPaymentSetByBorrowerInPercents(), loan.getBorrowerName());

        return loan;
    }
}
