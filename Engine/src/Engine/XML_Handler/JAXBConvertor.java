package Engine.XML_Handler;

import Engine.Customer;
import Engine.Loan;
import Engine.LoanPaymentsData;

public class JAXBConvertor {

    public static Customer convertCustomer(AbsCustomer generatedCustomer)
    {
        Customer customer = new Customer(generatedCustomer.name.trim(), generatedCustomer.absBalance);
        return customer;
    }

    public static Loan convertLoan(AbsLoan generatedLoan, int currentYaz)
    {
        Loan loan = new Loan(generatedLoan.id.trim(), generatedLoan.absOwner.trim(), generatedLoan.absCapital, generatedLoan.absTotalYazTime,
                generatedLoan.absPaysEveryYaz, generatedLoan.absIntristPerPayment, generatedLoan.absCategory.trim(), currentYaz);

        LoanPaymentsData loanPayments = new LoanPaymentsData(loan);
        loanPayments.createAllLoanPayments();

        return loan;
    }
}
