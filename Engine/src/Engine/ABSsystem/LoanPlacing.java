package Engine.ABSsystem;
import DTO.*;

import java.util.ArrayList;
import java.util.LinkedList;


public abstract class LoanPlacing {
    public enum LoanPlacementStatus{
        SUCCESS,
        FAILED
    }

    public LoanPlacementStatus placeToLoans(LoanPlacingDTO loanPlacingDto, LinkedList<Engine.Loan> loans){

        LinkedList<Engine.Loan> relevantLoans = this.getRelevantLoans(loanPlacingDto, loans);

        if(relevantLoans == null){ //there were no relevant loans
            return LoanPlacementStatus.FAILED;
        }

        ArrayList<LoanPlacingDBEntry> loansToPlaceMoney = createLoanPlacingDB(relevantLoans);
        placingAlgorithm(loansToPlaceMoney);
        implementActualPlacingToLoans(loansToPlaceMoney);

        return LoanPlacementStatus.SUCCESS;
    }

    private LinkedList<Engine.Loan> getRelevantLoans(LoanPlacingDTO loanPlacingDTO, LinkedList<Engine.Loan> loans){

    }

    private  ArrayList<LoanPlacingDBEntry> createLoanPlacingDB(LinkedList<Engine.Loan> relevantLoans){

    }

    private void placingAlgorithm(ArrayList<LoanPlacingDBEntry> loansDB){

    }

    private void implementActualPlacingToLoans(ArrayList<LoanPlacingDBEntry> loansDB){

    }

}
