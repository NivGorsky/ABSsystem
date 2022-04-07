package Engine;

import java.util.*;

public abstract class MoveTimeLine {

    private static int currentYaz;

    public static void moveTimeLineInOneYaz(SystemService absSystem){
        moveYaz(absSystem);
        currentYaz = absSystem.getTimeLine().getCurrentYaz();
        Map<String ,Customer> allCustomers = absSystem.getAllCustomers();
        LinkedList<Loan> sortedLoans = new LinkedList<Loan>();
        Customer currentCustomer = null;
        ArrayList<Loan> allLoansAsBorrower = null;

        for (Map.Entry<String, Customer> set:allCustomers.entrySet()){
            currentCustomer = set.getValue();
            allLoansAsBorrower = currentCustomer.getLoansAsBorrower();

            if(allLoansAsBorrower.isEmpty()){
                continue;
            }
            LinkedList<Loan> allRelevantLoans = getActiveAndExpiredLoansContainsPaymentsForCurrentYaz(allLoansAsBorrower, absSystem); //active loans that contain payment for curreny yaz
            allRelevantLoans.sort();
            IterateThroughSortedLoansAndMakePayments(allRelevantLoans, currentCustomer);
            allLoansAsBorrower.clear();
            sortedLoans.clear();
        }
    }

    private static void moveYaz(SystemService absSystem){
        Timeline systemTimeLine = absSystem.getTimeLine();
        systemTimeLine.moveTimeline(1);
    }

    private static LinkedList<Loan> getActiveAndExpiredLoansContainsPaymentsForCurrentYaz(ArrayList<Loan> allLoansAsBorrower, SystemService absSystem){
        LinkedList<Loan> onlyActiveAndInRiskLoans = new LinkedList<Loan>();

        for (Loan loan:allLoansAsBorrower){
            Loan.LoanStatus status = loan.getStatus();

           switch (status){
               case ACTIVE:
                   if(loan.peekPaymentForSpecificYaz(currentYaz) != null){
                       onlyActiveAndInRiskLoans.add(loan);
                   }

                   break;

               case IN_RISK: //means there are defenately expired payments
                       onlyActiveAndInRiskLoans.add(loan);

                   break;

               default:

                   break;
           }
        }

        return onlyActiveAndInRiskLoans;
    }

    private static boolean isBorrowerAbleToPayNextPayments(Customer currentBorrower, PriorityQueue<Loan> sortedLoans, SystemService absSystem){
        Account borrowerAccount = currentBorrower.getAccount();
        double balance = borrowerAccount.getBalance();
        Loan nextLoanToPay = sortedLoans.peek();
        int currentYaz = absSystem.getTimeLine().getCurrentYaz();
        boolean result = true;
        LoanPaymentsData.Payment nextPayment = nextLoanToPay.peekPaymentForSpecificYaz(currentYaz); //need to get exipred payments as well
        double nextPaymentAmount = nextPayment.getBothPartsOfAmountToPay();

        if(balance < nextPaymentAmount){ //there were not enough funds in borrower account to pay
            result = false;
        }

        return result;
    }

    private static void makePayment(Customer currentBorrower, PriorityQueue<Loan> sortedLoans, SystemService absSystem){
        Loan loanToPay = sortedLoans.poll();
        int currentYaz = absSystem.getTimeLine().getCurrentYaz();
        LoanPaymentsData.Payment payment = loanToPay.pollPaymentForSpecificYaz(currentYaz);
        Account loanAccount = loanToPay.getLoanAccount();

        absSystem.moveMoneyBetweenAccounts(currentBorrower.getAccount(), loanAccount, payment.getBothPartsOfAmountToPay());
        payment.setPaymentType(LoanPaymentsData.PaymentType.PAID);
        loanToPay.addNewPayment(payment);
    }

    private static void moveUnpaidActiveLoansToStatusInRisk(LinkedList<Loan> sortedLoans){
        for (Loan loan:sortedLoans){
            loan.setLoanStatus(Loan.LoanStatus.IN_RISK);
            LoanPaymentsData.Payment paymentToMoveToExpiredPayments = loan.pollPaymentForSpecificYaz(currentYaz);
            paymentToMoveToExpiredPayments.setPaymentType(LoanPaymentsData.PaymentType.EXPIRED);
            loan.addNewPayment(paymentToMoveToExpiredPayments);
        }
    }

    private static void IterateThroughSortedLoansAndMakePayments(LinkedList<Loan> allRelevantLoans, Customer borrower){

        for (Loan loan:allRelevantLoans){
            switch (loan.getStatus()){
                case IN_RISK:
                    makePaymentsForLoanThatIsInRisk(borrower);

                    break;
                case ACTIVE:
                    makePaymentsForLoanThatIsActive();

                    break;

                case NEW:
                    throw new Exception("In function IterateThroughSortedLoansAndMakePayments, loan should not be relevant, its NEW");

                    break;

                case FINISHED:
                    throw new Exception("In function IterateThroughSortedLoansAndMakePayments, loan should not be relevant, its FINISHED");

                    break;
                case PENDING:
                    throw new Exception("In function IterateThroughSortedLoansAndMakePayments, loan should not be relevant, its PENDING");
            }

        }
    }

    private static void makePaymentsForLoanThatIsInRisk(Loan loan, Customer borrower){
        SortedMap<Integer, LoanPaymentsData.Payment> expiredPayments = loan.getAllPayments(LoanPaymentsData.PaymentType.EXPIRED);
        Account borrowersAccount = borrower.getAccount();

        for (LoanPaymentsData.Payment payment:expiredPayments){
            if(isThereEnoughFundsForPayment(borowerAccount, payment)){
                makePayment(borrower,payment);
                changePaymentStatus()
                updatePaymentDataBase(key)
            }
        }

        if(!loan.isThereExpiredPayments()){
            changeLoanStatus(loan, active)
        }
    }

    private static void makePaymentsForLoanThatIsActive(Loan loan){
        LoanPaymentsData.Payment payment = loan.pollPaymentForSpecificYaz(currentYaz);

    }
}
