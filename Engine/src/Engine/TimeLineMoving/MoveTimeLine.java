package Engine.TimeLineMoving;

import Engine.*;

import java.util.*;

public abstract class MoveTimeLine {

    private static int currentYaz;

    public static void moveTimeLineInOneYaz(SystemService absSystem, Timeline timeLine)
    {
        moveYaz(absSystem);
        currentYaz = timeLine.getCurrentYaz();
        Map<String , Customer> allCustomers = absSystem.getAllCustomers();
        LinkedList<Loan> allRelevantLoansForCurrentBorrower = new LinkedList<Loan>();
        Customer currentCustomer = null;
        ArrayList<Loan> allLoansAsBorrower = null;

        for (Map.Entry<String, Customer> set:allCustomers.entrySet()){
            currentCustomer = set.getValue();
            allLoansAsBorrower = currentCustomer.getLoansAsBorrower();

            if(allLoansAsBorrower.isEmpty()){
                continue;
            }
            allRelevantLoansForCurrentBorrower = getActiveAndExpiredLoansContainsPaymentsForCurrentYaz(allLoansAsBorrower, absSystem); //active loans that contain payment for curreny yaz
            Collections.sort(allRelevantLoansForCurrentBorrower, new ForwardingYazLoanComparator());
            IterateThroughSortedLoansAndSendNotificatinos(allRelevantLoansForCurrentBorrower, currentCustomer, absSystem);
//            allLoansAsBorrower.clear();
            allRelevantLoansForCurrentBorrower.clear();
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
            loan.setLoanStatus(Loan.LoanStatus.IN_RISK, currentYaz);
            LoanPaymentsData.Payment paymentToMoveToExpiredPayments = loan.pollPaymentForSpecificYaz(currentYaz);
            paymentToMoveToExpiredPayments.setPaymentType(LoanPaymentsData.PaymentType.EXPIRED);
            loan.addNewPayment(paymentToMoveToExpiredPayments);
        }
    }

    private static void createNewNotificationForBorrower(Loan loan, Customer borrower, SystemService absSystem){
        Notification newNotification = new Notification();
        LoanPaymentsData.Payment payment = loan.peekPaymentForSpecificYaz(currentYaz);
        newNotification.amount = Double.toString(payment.getBothPartsOfAmountToPay());
        newNotification.loanName = loan.getLoanName();
        newNotification.yaz = Integer.toString(currentYaz);

        absSystem.addNotificationToCustomer(borrower, newNotification);
    }

    private static void IterateThroughSortedLoansAndSendNotificatinos(LinkedList<Loan> allRelevantLoans, Customer borrower, SystemService absSystem){

        for (Loan loan:allRelevantLoans){
            switch (loan.getStatus()){
                case IN_RISK:
//                    makePaymentsForLoanThatIsInRisk(loan, borrower, absSystem);
                    createNewNotificationForBorrower(loan, borrower, absSystem);

                    break;
                case ACTIVE:
//                    makePaymentsForLoanThatIsActive(loan, borrower, absSystem);
                    createNewNotificationForBorrower(loan, borrower, absSystem);

                    break;

                case NEW:
//                    throw new Exception("In function IterateThroughSortedLoansAndMakePayments, loan should not be relevant, its NEW");

                    break;

                case FINISHED:
//                    throw new Exception("In function IterateThroughSortedLoansAndMakePayments, loan should not be relevant, its FINISHED");

                    break;
                case PENDING:
//                    throw new Exception("In function IterateThroughSortedLoansAndMakePayments, loan should not be relevant, its PENDING");
            }

        }
    }

    private static void makePaymentsForLoanThatIsInRisk(Loan loan, Customer borrower, SystemService absSystem){
        Map<Integer, LoanPaymentsData.Payment> expiredPayments = (Map<Integer, Engine.LoanPaymentsData.Payment>)loan.getPayments(LoanPaymentsData.PaymentType.EXPIRED); //need to change to use polymorphysm
        Account borrowersAccount = borrower.getAccount();

        for (Map.Entry<Integer, LoanPaymentsData.Payment> set:expiredPayments.entrySet()){
            LoanPaymentsData.Payment currentPayment = set.getValue();

            if(isThereEnoughFundsForPayment(borrowersAccount, currentPayment)){
                makePayment(borrower,currentPayment, loan, absSystem);
                changePaymentStatus(loan, currentPayment, LoanPaymentsData.PaymentType.PAID);
                splitLoanMoneyToLenders(loan, absSystem);
            }
        }

        if(!loan.isTherePaymentsOfSpecificType(LoanPaymentsData.PaymentType.EXPIRED)){
            loan.setLoanStatus(Loan.LoanStatus.ACTIVE, currentYaz);
        }
    }

    private static void makePaymentsForLoanThatIsActive(Loan loan, Customer borrower, SystemService absSystem){
        LoanPaymentsData.Payment payment = loan.pollPaymentForSpecificYaz(currentYaz);
        Account borrowersAccount = borrower.getAccount();

        if(isThereEnoughFundsForPayment(borrowersAccount, payment)){
            makePayment(borrower,payment, loan, absSystem);
            changePaymentStatus(loan, payment, LoanPaymentsData.PaymentType.PAID);
            splitLoanMoneyToLenders(loan, absSystem);

            if(isLoanFinished(loan)){
                loan.setLoanStatus(Loan.LoanStatus.FINISHED, currentYaz);
            }
        }

        else{ //not enough funds to complete payment
            changePaymentStatus(loan, payment, LoanPaymentsData.PaymentType.EXPIRED);
            loan.setLoanStatus(Loan.LoanStatus.IN_RISK, currentYaz);
        }

    }

    private static boolean isLoanFinished(Loan loan){
        return !loan.isTherePaymentsOfSpecificType(LoanPaymentsData.PaymentType.EXPIRED) && !loan.isTherePaymentsOfSpecificType(LoanPaymentsData.PaymentType.UNPAID);
    }

    private static boolean isThereEnoughFundsForPayment(Account borrowersAccount, LoanPaymentsData.Payment payment){
        double amountToPay = payment.getBothPartsOfAmountToPay();
        double balance = borrowersAccount.getBalance();

        return balance >= amountToPay;
    }

    private static void makePayment(Customer borrower, LoanPaymentsData.Payment payment, Loan loan, SystemService absSystem){
        Account loanAccount = loan.getLoanAccount();
        Account borrowersAccount = borrower.getAccount();
        double amountToTransfer = payment.getBothPartsOfAmountToPay();

        if(loanAccount.getBalance() != 0){
           throw new RuntimeException("There was a problem while making a payment from borrower to loan - there is already money in loan account");
        }
        absSystem.moveMoneyBetweenAccounts(borrowersAccount, loanAccount, amountToTransfer);
    }

    private static void changePaymentStatus(Loan loan, LoanPaymentsData.Payment payment, LoanPaymentsData.PaymentType newType){
        payment.setPaymentType(newType);

        switch (newType){
            case PAID:
                payment.setActualPaymentYaz(currentYaz);
                break;

            case EXPIRED:
                break;

            case UNPAID:
                break;
        }


//        int paymentYaz = payment.getScheduledYaz();
//        loan.pollPaymentForSpecificYaz(paymentYaz);
        loan.addNewPayment(payment);
    }

    private static void splitLoanMoneyToLenders(Loan loan, SystemService absSystem){
        LinkedList<Loan.LenderDetails> lenders = loan.getLendersDetails();

        for (Loan.LenderDetails lenderDetails: lenders){
            Account lendersAccount = lenderDetails.lender.getAccount();
            Account loansAccount = loan.getLoanAccount();
            double amountInLoan = loansAccount.getBalance();
            double lendersPartOfLoanInPercent = lenderDetails.lendersPartOfLoanInPercent;
            double amountToTransfer = (lendersPartOfLoanInPercent / 100) * amountInLoan;

            absSystem.moveMoneyBetweenAccounts(loansAccount, lendersAccount, amountToTransfer);
        }



    }
}
