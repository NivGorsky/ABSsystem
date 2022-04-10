package UI.AssigningLoans;

import DTO.LoanPlacingDTO;
import UI.InputHandler;

import java.util.ArrayList;


public class AssignLoanToLenders {
    private final String customerName;
    private double amountToInvest;
    private ArrayList<String> categoriesWillingToInvestIn;
    private double minimumInterestPerYaz;
    private int minimumYazForReturn;
    private int maximumPercentOwnership;
    private int maximumOpenLoansForBorrower;
    String [] mandatoryFields = {"Amount to invest"};
    String [] OptionalFields = {"Categories list", "Minimum interest per yaz", "Minimum total yaz for loan"};

    public AssignLoanToLenders(String customerName, ArrayList<String> loanCategories){
        this.customerName = customerName;
        amountToInvest = -1;
        categoriesWillingToInvestIn = new ArrayList<String>();
        categoriesWillingToInvestIn.addAll(loanCategories);
        minimumInterestPerYaz = 0;
        minimumYazForReturn = - 1;
        maximumPercentOwnership = 100;
        maximumOpenLoansForBorrower = -1;
    }

    public LoanPlacingDTO getDTO()
    {
        return new LoanPlacingDTO(amountToInvest, categoriesWillingToInvestIn, minimumInterestPerYaz, minimumYazForReturn, maximumPercentOwnership, maximumOpenLoansForBorrower, customerName);
    }

    public void getAssigningParametersFromUser(){
        System.out.println("Please choose parameters for loan assigning\n");
        System.out.println("Mandatory fields:");
        System.out.println("Please enter amount to invest:\n");
        amountToInvest = InputHandler.getAmount();
        chooseOptionalFields();
    }

    private void chooseOptionalFields()
    {
        System.out.println("Optional fields: ");
        System.out.println("Please choose categories you would like to invest in: ");

        for (String category : categoriesWillingToInvestIn)
        {
            System.out.println(category);
        }

        System.out.println("How many categories would you like to choose? please choose a number between 0 to " +
                categoriesWillingToInvestIn.size());

        int numberOfCategories = InputHandler.getPositiveNumber();
        System.out.println("Please choose categories, seperated by Enter");
        categoriesWillingToInvestIn = InputHandler.chooseCategories(numberOfCategories, categoriesWillingToInvestIn); //init to all categories

        System.out.println("Please choose minimum interest per yaz, if you want to pass, enter -1");
        minimumInterestPerYaz = InputHandler.getMinInterestPerYaz();
        if(minimumInterestPerYaz == -1)
        {
            minimumInterestPerYaz = 0;
        }

        System.out.println("Please choose minimum total yaz for a loan, if you want to pass, enter -1"); //need to add the pass option
        minimumYazForReturn = InputHandler.getMinYazForReturn();
    }

}
