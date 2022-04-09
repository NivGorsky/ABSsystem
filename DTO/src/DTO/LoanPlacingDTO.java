package DTO;

import Engine.Loan;

import java.util.ArrayList;

public class LoanPlacingDTO {
    private final String customerName;
    private final double amountToInvest;
    private final ArrayList<String> catogoriesWillingToInvestIn; //TODO: REMANE
    private final double minimumInterestPerYaz;
    private final int minimumYazForReturn;
    private final int maximumPercentOwnership;
    private final int maximumOpenLoansForBorrower;

    public LoanPlacingDTO(double amountToInvest, ArrayList<String> categoriesWillingToInvestIn, double minimumInterestPerYaz, int minimumYazForReturn, int maximumPercentOwnership, int maximumOpenLoansForBorrower, String customerName){
        this.amountToInvest = amountToInvest;
        this.catogoriesWillingToInvestIn = categoriesWillingToInvestIn;
        this.minimumInterestPerYaz = minimumInterestPerYaz;
        this.minimumYazForReturn = minimumYazForReturn;
        this.maximumPercentOwnership = maximumPercentOwnership;
        this.maximumOpenLoansForBorrower = maximumOpenLoansForBorrower;
        this.customerName = customerName;
    }

    //getters
    public double getAmountToInvest(){return this.amountToInvest;}
    public ArrayList<String> getCatogoriesWillingToInvestIn(){return this.catogoriesWillingToInvestIn;}
    public double getMinimumInterestPerYaz() {
        return minimumInterestPerYaz;
    }
    public int getMinimumYazForReturn() {
        return minimumYazForReturn;
    }
    public int getMaximumPercentOwnership() {
        return maximumPercentOwnership;
    }
    public int getMaximumOpenLoansForBorrower() {
        return maximumOpenLoansForBorrower;
    }
    public String getCustomerName(){return this.customerName;}
}
