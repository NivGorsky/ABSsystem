package UI;

import Engine.MainSystem;
import Engine.ABSsystem;
import DTO.*;
import Exceptions.XMLFileException;
import UI.AssigningLoans.AssignLoanToLenders;

import javax.xml.bind.JAXBException;
import java.lang.System;
import java.util.ArrayList;


public class ConsoleUI {
    public enum MainMenu {
        LoadXML, ShowLoansInfo, ShowCustomersInfo, DepositMoney, WithdrawMoney, AssignLoansToLender,
        MoveTimeline, Exit
    }

    private final MainSystem engine = new ABSsystem();

    public void showMenu() {
        System.out.println("welcome to Alternative Bank System!");
        System.out.println("Please choose one of the options below (enter the number of the option):");
        System.out.println("1- Load XML file\n" +
                "2- Show information about all the loans in the system\n" +
                "3- Show information about all the customers in the system\n" +
                "4- Deposit money in account\n" +
                "5- Withdraw money from account\n" +
                "6- Invest money in loans\n" +
                "7- Move the timeline\n" +
                "8- Exit\n\n");
    }

    public String chooseCustomer()
    {
        int userChoice, i = 1;
        ArrayList<String> customersNames;

        customersNames = engine.getCustomersNames();
        System.out.println("Please choose a customer:");

        for (String name : customersNames) {
            System.out.println(i + ". " + name);
            ++i;
        }

        userChoice = InputHandler.getCustomer(customersNames.size());
        return customersNames.get(userChoice-1);
    }

    public CustomerDTO chooseCustomerWithBalance()
    {
        ArrayList<CustomerDTO> customers = engine.showCustomersInfo();
        System.out.println("Please choose a customer:");
        int userChoice, i=1;

        for(CustomerDTO c : customers)
        {
            System.out.println(i + ". name: " + c.getCustomerName() + " balance: " + c.getBalance());
            ++i;
        }

        userChoice = InputHandler.getCustomer(customers.size());
        return customers.get(userChoice -1);
    }

    public double chooseAmount(String action)
    {
        System.out.println("Please enter amount of money you would like to " + action + ":");
        return  InputHandler.getAmount();
    }

    public void runMainMenu() //main menu
    {
        int input;
        boolean isFileLoaded = false;
        MainMenu userChoice = MainMenu.LoadXML; //initialized to something != exit

        while (userChoice != MainMenu.Exit) {
            showMenu();

            input = InputHandler.getOptionFromMenu();
            userChoice = MainMenu.values()[input-1];
            try {
                switch (userChoice) {
                    case LoadXML:
                    {
                        boolean currFile = loadXML();
                        if(!isFileLoaded && currFile)
                        {
                            isFileLoaded = true;
                        }
                        break;
                    }
                    case ShowLoansInfo:
                    {
                        if (checkFileLoaded(isFileLoaded)) {
                            showLoansInfo();
                            break;
                        }
                    }
                    case ShowCustomersInfo:
                    {
                        if (checkFileLoaded(isFileLoaded)) {
                            showCustomersInfo();
                            break;
                        }
                    }
                    case DepositMoney:
                    {
                        if (checkFileLoaded(isFileLoaded)) {
                            depositMoney();
                            break;
                        }
                    }
                    case WithdrawMoney:
                    {
                        if (checkFileLoaded(isFileLoaded)) {
                            withdrawMoney();
                            break;
                        }
                    }
                    case AssignLoansToLender: {

                        if (checkFileLoaded(isFileLoaded))
                        {
                            assignLoansToLender();
                            break;
                        }
                    }
                    case MoveTimeline: {

                        if (checkFileLoaded(isFileLoaded)) {
                            moveTimeline();
                            break;
                        }
                    }
                    case Exit: {
                        System.out.println("Bye bye");
                        System.exit(1);
                    }
                }
            }
            catch (XMLFileException ex) {
                System.out.println(ex.getExceptionMsg());
            }
        }
    }

    public boolean loadXML()
    {
        boolean isFileLoaded = false;

        System.out.println("1- Load XML file\n");
        System.out.println("Please enter a path to the XML file (the path cannot contain hebrew letters)");
        String path = InputHandler.getPathToFile();

        try{
            engine.loadXML(path);
            System.out.println("File loaded successfully!\n");
            isFileLoaded = true;
        }

        catch (JAXBException e) {
            e.printStackTrace();
         }
        catch (XMLFileException ex) {
            System.out.println(ex.getExceptionMsg());

        }

        return isFileLoaded;
    }

    public void showLoansInfo() {

        ArrayList<LoanDTO> loans = engine.showLoansInfo();

        System.out.println("2- Show information about all the loans in the system\n");

        for (LoanDTO l : loans)
        {
            System.out.println(l.toString());
        }
    }

    public void showCustomersInfo()
    {
        ArrayList<CustomerDTO> customers = engine.showCustomersInfo();

        System.out.println("3- Show information about all the customers in the system\n");

        for(CustomerDTO c : customers)
        {
            System.out.println(c.toString());
        }
    }

    public void depositMoney()
    {
        System.out.println("4- Deposit money in account\n");
        try {
            String customerName = chooseCustomer();
            double amount = chooseAmount("deposit");
            engine.depositMoney(customerName, amount);
            System.out.println("Deposit succeeded!");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void withdrawMoney()
    {
        String customerName = chooseCustomer();

        System.out.println("5- Withdraw money from account\n");
        double amount = chooseAmount("withdraw");

        try {
            engine.withdrawMoney(customerName, amount);
            System.out.println("Withdraw succeeded!");
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void assignLoansToLender(){
        //get the data from user and create dto, then send it to Engine
        try
        {
            System.out.println("6- Invest money in loans\n");
            CustomerDTO chosenCustomer = chooseCustomerWithBalance();
            AssignLoanToLenders assignLoanToLendersForm = new AssignLoanToLenders(chosenCustomer.getCustomerName(), this.engine.getSystemLoanCategories());
            assignLoanToLendersForm.getAssigningParametersFromUser();
            engine.assignLoansToLender(assignLoanToLendersForm.getDTO());
        }

        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void moveTimeline()
    {
        System.out.println("7- Move the timeline\n");
        TimelineDTO systemTimeline = engine.moveTimeLine();
        System.out.println("Action succeeded!");
        System.out.println(systemTimeline.toString());
    }

    private boolean checkFileLoaded(boolean isFileLoaded) throws XMLFileException
    {
        if(!isFileLoaded)
        {
            throw new XMLFileException("XML file not loaded!");
        }

        return true;
    }
}
