package Engine;
import DTO.*;

import java.util.ArrayList;
import java.util.LinkedList;


public abstract class LoanPlacing {
    public enum LoanPlacementStatus{
        SUCCESS,
        FAILED
    }

    public static LoanPlacementStatus placeToLoans(LoanPlacingDTO loanPlacingDto, LinkedList<Engine.Loan> loans, SystemService absService){
        LinkedList<Engine.Loan> relevantLoans = getRelevantLoans(loanPlacingDto, loans, absService);

        if(relevantLoans.isEmpty()){ //there were no relevant loans
            return LoanPlacementStatus.FAILED;
        }
        LinkedList<LoanPlacingDBEntry> loansToPlaceMoney = createLoanPlacingDB(relevantLoans);
        placingAlgorithm(loansToPlaceMoney, loanPlacingDto, absService);

        return LoanPlacementStatus.SUCCESS;
    }

    private static void placingAlgorithm(LinkedList<LoanPlacingDBEntry> openLoansDB, LoanPlacingDTO dto, SystemService absService){
        double amountLeftToInvest = dto.getAmountToInvest();
        int numberOfOpenLoans = openLoansDB.size();
        double amountToPutInEachLoan = amountLeftToInvest / numberOfOpenLoans;
//        LinkedList<LoanPlacingDBEntry> loansPassedAmountThreshold = new LinkedList<LoanPlacingDBEntry>();
        LinkedList<LoanPlacingDBEntry> closedLoans = new LinkedList<LoanPlacingDBEntry>();

        while(amountLeftToInvest > 0 && numberOfOpenLoans > 0){
            putMoneyInEachOpenLoan(openLoansDB, amountToPutInEachLoan);
            amountLeftToInvest = moveClosedAndPassedThresholdLoansAndReturnGapToAmountLeftToLend(openLoansDB, closedLoans, amountLeftToInvest);
            numberOfOpenLoans = openLoansDB.size();

            if(numberOfOpenLoans == 0){
                break;
            }
            amountToPutInEachLoan = amountLeftToInvest / numberOfOpenLoans;
        }
        transferMoneyToLoansAccountsAndRegisterLenders(closedLoans, dto, absService);
    }

    private static void putMoneyInEachOpenLoan(LinkedList<LoanPlacingDBEntry> openLoansDB, double amountToPutInEachLoan){
        for (LoanPlacingDBEntry loanEntry:openLoansDB){
            loanEntry.amountToLend += amountToPutInEachLoan;
        }
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

    private static void transferMoneyToLoansAccountsAndRegisterLenders(LinkedList<LoanPlacingDBEntry> loansToTransfer, LoanPlacingDTO dto, SystemService absService){
        double currentAmountToTransfer = -1;
        Customer lender = absService.getCustomerByName(dto.getCustomerName());
        Account lendersAccount = lender.getAccount();
        Account loansAccount = null;

        for (LoanPlacingDBEntry loanEntry: loansToTransfer){
            currentAmountToTransfer = loanEntry.amountToLend;
            loansAccount = loanEntry.loan.getLoanAccount();
            absService.moveMoneyBetweenAccounts(lendersAccount, loansAccount, currentAmountToTransfer);
            loanEntry.loan.addNewLender(lender, currentAmountToTransfer);
        }
    }

    private static LinkedList<Engine.Loan> getRelevantLoans(LoanPlacingDTO loanPlacingDTO, LinkedList<Engine.Loan> allLoansInSystem, SystemService absService){
        LinkedList<Engine.Loan> relevantLoans = new LinkedList<Loan>();

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

        // i check what turns the decision to no
        if(!loanDto.getCategoriesWillingToInvestIn().contains(loan.getCategory())){
            result = false;
        }

        else if(loanDto.getMinimumInterestPerYaz() > loan.getInterestPerPaymentSetByBorrowerInPercents()){
            result = false;
        }

//        else if(loanDto.getMinimumYazForReturn()) need to verify what does that mean

        else if(loanDto.getMaximumPercentOwnership() >  100 - loan.getLoanPercentageTakenByLenders()){
            result = false;
        }


        else if(loanDto.getMaximumOpenLoansForBorrower() > maximumOpenLoansForLoanBorrower){
            result = false;
        }

        return result;
    }

    private static  LinkedList<LoanPlacingDBEntry> createLoanPlacingDB(LinkedList<Engine.Loan> relevantLoans){
        LinkedList<LoanPlacingDBEntry> loanPlacingDB = new LinkedList<LoanPlacingDBEntry>();

        for (Engine.Loan l: relevantLoans){
             LoanPlacingDBEntry newDatum = createLoanPlacingDBDatumFromLoan(l);
             loanPlacingDB.add(newDatum);
        }

        return loanPlacingDB;
    }

    private static LoanPlacingDBEntry createLoanPlacingDBDatumFromLoan(Engine.Loan l){
        LoanPlacingDBEntry newDatum = new LoanPlacingDBEntry();
        newDatum.loan = l;
        newDatum.amountOpenToLending = l.getLoanAmountFinancedByLenders();
        newDatum.amountToLend = 0;

        return newDatum;
    }

}
