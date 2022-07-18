package Engine.LoanPlacing.loanPlacingAsTask;

import DTO.LoanPlacingDTO;
import Engine.Account;
import Engine.Customer;
import Engine.Loan;
import Engine.SystemService;
import Exceptions.DataBaseAccessException;
import Exceptions.SystemRestrictionsException;
import javafx.concurrent.Task;
import java.util.LinkedList;
import java.util.function.Consumer;


public class LoanPlacingAsTask extends Task<Boolean> {

    private Consumer<Integer> numberOfLoansAssigned;
    private LoanPlacingDTO loanPlacingDTO;
    private LinkedList<Loan> loans;
    private SystemService absService;
    private int currentYaz;
    private int SLEEP_TIME_MIILIS = 300;

    public LoanPlacingAsTask(Consumer<Integer> numberOfLoansAssignedConsumer,LoanPlacingDTO loanPlacingDto, LinkedList<Engine.Loan> loans, SystemService absService, int currentYazReceived){
        this.numberOfLoansAssigned = numberOfLoansAssignedConsumer;
        this.loanPlacingDTO = loanPlacingDto;
        this.loans = loans;
        this.absService = absService;
        this.currentYaz = currentYazReceived;
    }

    @Override
    protected Boolean call() throws Exception{
        try{
            placeToLoans(this.loanPlacingDTO, this.loans, this.absService);
        }

        catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    public void placeToLoans(LoanPlacingDTO loanPlacingDto, LinkedList<Loan> loans, SystemService absService) throws Exception
    {
        updateProgress(0,1);
        updateMessage("Searching for relevant loans");
        Thread.sleep(SLEEP_TIME_MIILIS);
        LinkedList<Loan> relevantLoans = getRelevantLoans(loanPlacingDto, loans, absService);
        LinkedList<Loan> loansMadePaymentsTo;

        if(relevantLoans.isEmpty()){ //there were no relevant loans
            throw new SystemRestrictionsException(null, "There are no relevant loans");
        }
//        LinkedList<LoanPlacingDBEntry> loansToPlaceMoney = createLoanPlacingDB(relevantLoans);
        loansMadePaymentsTo = placingAlgorithm(relevantLoans, loanPlacingDto, absService);
        changeLoansStatuses(loansMadePaymentsTo);

        updateMessage("Finished!");
        updateProgress(1,1);
        numberOfLoansAssigned.accept(loansMadePaymentsTo.size());
        Thread.sleep(SLEEP_TIME_MIILIS);
    }

    private LinkedList<Loan> placingAlgorithm(LinkedList<Loan> relevantLoans, LoanPlacingDTO dto, SystemService absService) throws Exception
    {
        //closed loans - loans that were exactly filled to their requested amount
        //open loans - loans that are still waiting for more lenders, have not passed the initial amount threshold
        updateMessage("Placing equaly to loans");
        Thread.sleep(SLEEP_TIME_MIILIS);

        LinkedList<LoanPlacingDBEntry> openLoansDB = createLoanPlacingDB(relevantLoans);
        LinkedList<Loan> loansMadePaymentsTo = new LinkedList<>();
        double amountLeftToInvest = dto.getAmountToInvest();
        double initialAmount = amountLeftToInvest;
        int numberOfOpenLoans = openLoansDB.size();
        double amountToPutInEachLoan = amountLeftToInvest / numberOfOpenLoans;

        LinkedList<LoanPlacingDBEntry> closedLoansDB = new LinkedList<>();
        LinkedList<LoanPlacingDBEntry> loanEntriesToMakeActualPayments = new LinkedList<>();

        while(amountLeftToInvest > 0.001 && numberOfOpenLoans > 0){
            updateProgress(((initialAmount - amountLeftToInvest) / initialAmount), 1);
            Thread.sleep(SLEEP_TIME_MIILIS);

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

    private LinkedList<Loan> getLoansMadePaymentsTo(LinkedList<LoanPlacingDBEntry> loanEntries){
        LinkedList<Loan> actualLoans = new LinkedList<>();

        for (LoanPlacingDBEntry entry:loanEntries){
            actualLoans.add(entry.loan);
        }

        return actualLoans;
    }

    private void changeLoansStatuses(LinkedList<Loan> loansMadePaymentsTo){
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

    private double putMoneyInEachOpenLoan(LinkedList<LoanPlacingDBEntry> openLoansDB, double amountToPutInEachLoan, double amountLeftToInvest){
        for (LoanPlacingDBEntry loanEntry:openLoansDB){
            loanEntry.amountToLend += amountToPutInEachLoan;
            amountLeftToInvest -= amountToPutInEachLoan;
        }

        return amountLeftToInvest;
    }

    private double moveClosedAndPassedThresholdLoansAndReturnGapToAmountLeftToLend(LinkedList<LoanPlacingDBEntry> openLoansDB, LinkedList<LoanPlacingDBEntry> closedLoans, double amountLeftToLend){
        double updatedAmountLeftToLend = amountLeftToLend;

        for (int i = 0; i < openLoansDB.size(); i++) {

            LoanPlacingDBEntry loanEntry = openLoansDB.get(i);

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
                openLoansDB.remove(i);
            }
        }

        return updatedAmountLeftToLend;
    }

    private void transferMoneyToLoansAccountsAndRegisterLenders(LinkedList<LoanPlacingDBEntry> loansToTransfer, LoanPlacingDTO dto, SystemService absService) throws Exception
    {
        double currentAmountToTransfer = -1;
        Customer lender = absService.getCustomerByName(dto.getCustomerName());
        String borrowerName;
        Customer borrower;
        Account lendersAccount = lender.getAccount();
        Account loansAccount = null;

        for (LoanPlacingDBEntry loanEntry: loansToTransfer)
        {
            borrowerName = loanEntry.loan.getBorrowerName();
            borrower = absService.getCustomerByName(borrowerName);
            Account borrowersAccount = borrower.getAccount();
            currentAmountToTransfer = loanEntry.amountToLend;
            loansAccount = loanEntry.loan.getLoanAccount();
            absService.moveMoneyBetweenAccounts(lendersAccount, loansAccount, currentAmountToTransfer);
            //absService.moveMoneyBetweenAccounts(loansAccount, borrowersAccount, currentAmountToTransfer);
            loanEntry.loan.addNewLender(lender, currentAmountToTransfer);
            lender.addLoanAsLender(loanEntry.loan);
        }
    }

    private LinkedList<Loan> getRelevantLoans(LoanPlacingDTO loanPlacingDTO, LinkedList<Loan> allLoansInSystem, SystemService absService){
        LinkedList<Loan> relevantLoans = new LinkedList<>();

        for (Loan loan:allLoansInSystem) {
            if(isLoanRelevant(loanPlacingDTO, loan, absService)){
                relevantLoans.add(loan);
            }
        }

        return relevantLoans;
    }

    private boolean isLoanRelevant(LoanPlacingDTO loanDto, Loan loan, SystemService absService){
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

        else if(!isLoanStatusAllowsMoreInvestments(loan)){
            result = false;
        }

//        else if(loanDto.getMinimumYazForReturn()) need to verify what does that mean

        else if(loanDto.getMaximumPercentOwnership() < 100 - loan.getLoanPercentageTakenByLenders()){
            if(loanDto.getMaximumPercentOwnership() != -1){
                result = false;
            }
        }

        else if(maximumOpenLoansForLoanBorrower > loanDto.getMaximumOpenLoansForBorrower()){
            if(loanDto.getMaximumOpenLoansForBorrower() != -1){
                result = false;
            }
        }

        return result;
    }

    private LinkedList<LoanPlacingDBEntry> createLoanPlacingDB(LinkedList<Loan> relevantLoans){
        try {
            LinkedList<LoanPlacingDBEntry> loanPlacingDB = new LinkedList<LoanPlacingDBEntry>();

            for (Loan l : relevantLoans) {
                LoanPlacingDBEntry newDatum = createLoanPlacingDBDatumFromLoan(l);
                loanPlacingDB.add(newDatum);
            }

            return loanPlacingDB;
        }

        catch (Exception e){
            throw new DataBaseAccessException(null, "There was a problem while trying to createLoanPlacingDB ");
        }
    }

    private boolean isLoanStatusAllowsMoreInvestments(Loan l){
        return l.getStatus() == Loan.LoanStatus.PENDING || l.getStatus() == Loan.LoanStatus.NEW;
    }

    private LoanPlacingDBEntry createLoanPlacingDBDatumFromLoan(Loan l){
        LoanPlacingDBEntry newDatum = new LoanPlacingDBEntry();
        newDatum.loan = l;
        newDatum.amountOpenToLending = l.getInitialAmount() - l.getLoanAmountFinancedByLenders();
        newDatum.amountToLend = 0;

        return newDatum;
    }

}
