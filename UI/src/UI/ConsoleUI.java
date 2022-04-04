package UI;

import DTO.*;
import Engine.*;
import java.lang.System;
import java.util.ArrayList;
import java.util.Scanner;


public class ConsoleUI {
    public enum MainMenu {
        LoadXML, ShowLoansInfo, ShowCustomersInfo, DepositMoney, WithdrawMoney, AssignLoansToLender,
        MoveTimeline, Exit
    }

    private MainSystem engine = new ABSsystem();
    private MainMenu menu;

    public void showMenu()
    {
        System.out.println("welcome to Alternative Bank System!");
        System.out.println("Please choose one of the options below (enter the number of the option):");
        System.out.println("1- Load XML file\n" +
                "2- Show information about all the loans in the system\n" +
                "3- Show information about all the customers in the system\n" +
                "4- Deposit money in account\n" +
                "5- Withdraw money from account\n" +
                "6- Invest money in loans\n" +
                "7- Move the timeline\n" +
                "8- Exit");
    }

    public String chooseCustomer()
    {
        int userChoice, i = 0;
        ArrayList<String> customersNames;

        customersNames = (ArrayList<String>)engine.getCustomersNames();
        System.out.println("Please choose a customer:");

        for (String name : customersNames)
        {
            System.out.println(i + "- " + name);
        }

        Scanner sc = new Scanner(System.in);
        userChoice = sc.nextInt();
        //while(check input not ok)//TODO input check
        //choose customer again (or maybe back to main menu?)

        return customersNames.get(userChoice);
    }

    public double chooseAmount(String action)
    {
        double amount;

        //TODO while(check input not ok)
        System.out.println("Please enter amount of money you would like to " + action + ":");

        Scanner sc = new Scanner(System.in);
        amount = sc.nextDouble();

        return amount;
    }

    public void getUserChoice() //main menu
    {
        int input;
        MainMenu userChoice = MainMenu.LoadXML; //initilize to somthing != exit
        Scanner sc = new Scanner(System.in);

        while(userChoice!= MainMenu.Exit)
        {
            showMenu();
            input = sc.nextInt();
            //TODO input check
            userChoice = MainMenu.values()[input];

            switch (userChoice)
            {
                case LoadXML:
                {
                    loadXML();
                    break;
                }
                case ShowLoansInfo:
                {
                    showLoansInfo();
                    break;
                }
                case ShowCustomersInfo:
                {
                    showCustomersInfo();
                    break;
                }
                case DepositMoney:
                {
                    String customerName = chooseCustomer();
                    double amount = chooseAmount("deposit");
                    engine.depositMoney(customerName, amount);
                    System.out.println("Deposit succeeded!");
                    break;
                }
                case WithdrawMoney:
                {
                    String customerName = chooseCustomer();
                    double amount = chooseAmount("withdraw");

                    try{
                        engine.withdrawMoney(customerName, amount);
                        System.out.println("Withdraw succeeded!");
                    }
                    catch(Exception ex)
                    {
                        System.out.println(ex.getMessage());
                    }
                    break;
                }
                case AssignLoansToLender:
                {
                    //show all customers
                    //another input - customer
                    //insert parameters
                    break;
                }
                case MoveTimeline:
                {
                    moveTimeline();
                    break;
                }
                case Exit:
                {
                    System.exit(1);
                }

            }
        }
    }

    public void loadXML() {}; //TODO

    public void showLoansInfo() {

        ArrayList<LoanDTO> loans = engine.showLoansInfo();

        for (LoanDTO l : loans)
        {
            System.out.println("ABSsystem.Loan id: " + l.getLoanId());
            System.out.println("Loaner name: " + l.getCustomerName());
            System.out.println("Category: " + l.getCategory());
            System.out.println("Total loan amount: " + l.getInitialAmount());
            System.out.println("Total time to repay the loan: " + l.getMaxYazToPay());
            System.out.println("Interest per payment: " + l.getInterestPerPayment() + "%");
            System.out.println("Payment will be made every " + l.getYazPerPayment() + " yaz");
            System.out.println("The loan is in status  " + l.getStatus() + ": ");

            System.out.println("Registered lenders for loan:");
            for(String[] lender : l.getLendersNamesAndAmounts())
            {
                System.out.println("Name: " + lender[0] + "  Investment amount: " + lender[1]);
            }

            if(l.getStatus() == Loan.LoanStatus.PENDING)
            {
                System.out.println("Total money raised: " + l.getTotalMoneyRaised());
                System.out.println("There are " + (l.getInitialAmount() - l.getTotalMoneyRaised()) + " left to make the loan active");
            }
            if(l.getStatus() == Loan.LoanStatus.ACTIVE)
            {
                System.out.println("The loan became active in yaz number " + l.getActivationYaz());
                System.out.println("The next payment is in yaz number "); //TODO

            }




        }


    } //TODO

    public void showCustomersInfo()
    {
        ArrayList<CustomerDTO> customers = engine.showCustomersInfo();

        for(CustomerDTO c : customers)
        {
            System.out.println("ABSsystem.Customer Name: " + c.getCustomerName());

            for(AccountMovementDTO movement : c.getAccountMovements())
            {
                c.toString();
            }
        }



    } //TODO

    public void depositMoney(String custName, int amount) {}; //TODO
    public void withdrawMoney(String custName, int amount){};//TODO
    public void assignLoansToLender(){};
    public void moveTimeline(){};


}
