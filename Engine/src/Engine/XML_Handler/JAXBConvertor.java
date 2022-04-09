package Engine.XML_Handler;

import Engine.Customer;
import Engine.Loan;
import Engine.LoanPaymentsData;

public class JAXBConvertor {

    public static Customer convertCustomer(AbsCustomer generatedCustomer)
    {
        Customer customer = new Customer(generatedCustomer.name, generatedCustomer.absBalance);
        return customer; //TODO:add customer to the map in system
    }

    public static Loan convertLoan(AbsLoan generatedLoan)
    {
        Loan loan = new Loan(generatedLoan.id, generatedLoan.absOwner, generatedLoan.absCapital, generatedLoan.absTotalYazTime,
                generatedLoan.absPaysEveryYaz, generatedLoan.absIntristPerPayment, generatedLoan.absCategory);

        //TODO: fix loanId and loanName - dont need both i think name==id
        LoanPaymentsData loanPayments = new LoanPaymentsData(loan);
        loanPayments.createAllLoanPayments();

        return loan;
    }



}
