package Engine.LoanPlacing;
import DTO.*;
import Engine.Account;
import Engine.Customer;
import Engine.Loan;
import Engine.SystemService;
import Exceptions.DataBaseAccessException;
import Exceptions.SystemRestrictionsException;
import java.util.LinkedList;


public abstract class LoanPlacing {

    private static int currentYaz;

    public static void placeToLoans(LoanPlacingDTO loanPlacingDto, LinkedList<Engine.Loan> loans, SystemService absService, int currentYaz) throws Exception
    {
        LinkedList<Engine.Loan> relevantLoans = getRelevantLoans(loanPlacingDto, loans, absService);
        LinkedList<Loan> loansMadePaymentsTo;

        if(relevantLoans.isEmpty()){ //there were no relevant loans
            throw new SystemRestrictionsException(null, "There are no relevant loans");
        }
//        LinkedList<LoanPlacingDBEntry> loansToPlaceMoney = createLoanPlacingDB(relevantLoans);
        loansMadePaymentsTo = placingAlgorithm(relevantLoans, loanPlacingDto, absService);
        changeLoansStatuses(loansMadePaymentsTo);
    }

    private static  LinkedList<Loan> placingAlgorithm(LinkedList<Loan> relevantLoans, LoanPlacingDTO dto, SystemService absService) throws Exception
    {
        //closed loans - loans that were exactly filled to their requested amount
        //open loans - loans that are still waiting for more lenders, have not passed the initial amount threshold
        LinkedList<LoanPlacingDBEntry> openLoansDB = createLoanPlacingDB(relevantLoans);
        LinkedList<Loan> loansMadePaymentsTo = new LinkedList<>();
        double amountLeftToInvest = dto.getAmountToInvest();
        int numberOfOpenLoans = openLoansDB.size();
        double amountToPutInEachLoan = amountLeftToInvest / numberOfOpenLoans;

        LinkedList<LoanPlacingDBEntry> closedLoansDB = new LinkedList<>();
        LinkedList<LoanPlacingDBEntry> loanEntriesToMakeActualPayments = new LinkedList<>();

        while(amountLeftToInvest > 0.001 && numberOfOpenLoans > 0){
            amountLeftToInvest = putMoneyInEachOpenLoan(openLoansDB, amountToPutInEachLoan, amountLeftToInvest);
            amountLeftToInvest = moveClosedAndPassedThresholdLoansAndReturnGapToAmountLeftToLend(openLoansDB, closedLoansDB, amountLeftToInvest);
            numberOfOpenLoans = openLoansDB.size();

            if(numberOfOpenLoans == 0){
                break;
            }
            amountToPutInEachLoan = amountLeftToInvest / numberOfOpenLoans;
        }
        loanEntriesToMakeActualPayments.addAll(closedLoansDB);
        loanEntriesToMakeActualPayments.addAll(openLoansDB);
        transferMoneyToLoansAccountsAndRegisterLenders(loanEntriesToMakeActualPayments, dto, absService);
        loansMadePaymentsTo = getLoansMadePaymentsTo(loanEntriesToMakeActualPayments);

        return loansMadePaymentsTo;
    }

    private static LinkedList<Loan> getLoansMadePaymentsTo(LinkedList<LoanPlacingDBEntry> loanEntries){
        LinkedList<Loan> actualLoans = new LinkedList<>();

        for (LoanPlacingDBEntry entry:loanEntries){
            actualLoans.add(entry.loan);
        }

        return actualLoans;
    }

    private static void changeLoansStatuses(LinkedList<Loan> loansMadePaymentsTo){
        for (Loan loan:loansMadePaymentsTo){

            if(loan.getInitialAmount() == loan.getLoanAmountFinancedByLenders()){
                loan.setLoanStatus(Loan.LoanStatus.ACTIVE, currentYaz);
            }

            else if(loan.getInitialAmount() > loan.getLoanAmountFinancedByLenders()){
                loan.setLoanStatus(Loan.LoanStatus.PENDING, currentYaz);
            }

            else{
                throw new RuntimeException("Loan: " + loan + "raised more money than possible");
            }
        }
    }

    private static double putMoneyInEachOpenLoan(LinkedList<LoanPlacingDBEntry> openLoansDB, double amountToPutInEachLoan, double amountLeftToInvest){
        for (LoanPlacingDBEntry loanEntry:openLoansDB){
            loanEntry.amountToLend += amountToPutInEachLoan;
            amountLeftToInvest -= amountToPutInEachLoan;
        }

        return amountLeftToInvest;
    }

    private static double moveClosedAndPassedThresholdLoansAndReturnGapToAmountLeftToLend(LinkedList<LoanPlacingDBEntry> openLoansDB,LinkedList<LoanPlacingDBEntry> closedLoans, double amountLeftToLend){
        double updatedAmountLeftToLend = amountLeftToLend;

        for (LoanPlacingDBEntry loanEntry:openLoansDB){
            //loan is closed
            if(loanEntry.amountOpenToLending == loanEntry.amountToLend){
                closedLoans.add(loanEntry);
                openLoansDB.remove(loanEntry);
            }

            //loan has passed threshold
            else if(loanEntry.amountToLend > loanEntry.amountOpenToLending){
                double gap = loanEntry.amountToLend - loanEntry.amountOpenToLending;
                loanEntry.amountToLend = loanEntry.amountOpenToLending;
                updatedAmountLeftToLend += gap; //returning the money to total
                closedLoans.add(loanEntry);
                openLoansDB.remove(loanEntry);
            }
        }

        return updatedAmountLeftToLend;
    }

    private static void transferMoneyToLoansAccountsAndRegisterLenders(LinkedList<LoanPlacingDBEntry> loansToTransfer, LoanPlacingDTO dto, SystemService absService) throws Exception
    {
        double currentAmountToTransfer = -1;
        Customer lender = absService.getCustomerByName(dto.getCustomerName());
        Account lendersAccount = lender.getAccount();
        Account loansAccount = null;

        for (LoanPlacingDBEntry loanEntry: loansToTransfer)
        {
            currentAmountToTransfer = loanEntry.amountToLend;
            loansAccount = loanEntry.loan.getLoanAccount();
            absService.moveMoneyBetweenAccounts(lendersAccount, loansAccount, currentAmountToTransfer);
            loanEntry.loan.addNewLender(lender, currentAmountToTransfer);
            lender.addLoanAsLender(loanEntry.loan);
        }
    }

    private static LinkedList<Engine.Loan> getRelevantLoans(LoanPlacingDTO loanPlacingDTO, LinkedList<Engine.Loan> allLoansInSystem, SystemService absService){
        LinkedList<Engine.Loan> relevantLoans = new LinkedList<>();

        for (Engine.Loan loan:allLoansInSystem) {
            if(isLoanRelevant(loanPlacingDTO, loan, absService)){
                relevantLoans.add(loan);
            }
        }

        return relevantLoans;
    }

    private static boolean isLoanRelevant(LoanPlacingDTO loanDto, Engine.Loan loan, SystemService absService){
        boolean result = true;
        //get max loans open for borrower
        String borrowerName = loan.getBorrowerName();
        Customer borrower =  absService.getCustomerByName(borrowerName);
        int maximumOpenLoansForLoanBorrower = borrower.getLoansAsBorrower().size();

        if(!loanDto.getCategoriesWillingToInvestIn().contains(loan.getCategory())){
            result = false;
        }

        //
        else if(loan.getBorrowerName().equals(loanDto.getCustomerName())){
            result = false;
        }

        else if(loanDto.getMinimumInterestPerYaz() > loan.getInterestPerPaymentSetByBorrowerInPercents()){
            if(loanDto.getMinimumInterestPerYaz() != -1){
                result = false;
            }
        }

//        else if(loanDto.getMinimumYazForReturn()) need to verify what does that mean

//        else if(loanDto.getMaximumPercentOwnership() >  100 - loan.getLoanPercentageTakenByLenders()){
//            if(loanDto.getMaximumPercentOwnership() != -1){
//                result = false;
//            }
//        }

        else if(loanDto.getMaximumOpenLoansForBorrower() > maximumOpenLoansForLoanBorrower){
            if(loanDto.getMaximumOpenLoansForBorrower() != -1){
                result = false;
            }
        }

        return result;
    }

    private static  LinkedList<LoanPlacingDBEntry> createLoanPlacingDB(LinkedList<Engine.Loan> relevantLoans){
        try {
            LinkedList<LoanPlacingDBEntry> loanPlacingDB = new LinkedList<LoanPlacingDBEntry>();

            for (Engine.Loan l : relevantLoans) {
                LoanPlacingDBEntry newDatum = createLoanPlacingDBDatumFromLoan(l);
                loanPlacingDB.add(newDatum);
            }

            return loanPlacingDB;
        }

        catch (Exception e){
            throw new DataBaseAccessException(null, "There was a problem while trying to createLoanPlacingDB ");
        }
    }

    private static LoanPlacingDBEntry createLoanPlacingDBDatumFromLoan(Engine.Loan l){
        LoanPlacingDBEntry newDatum = new LoanPlacingDBEntry();
        newDatum.loan = l;
        newDatum.amountOpenToLending = l.getInitialAmount() - l.getLoanAmountFinancedByLenders();
        newDatum.amountToLend = 0;

        return newDatum;
    }

}
