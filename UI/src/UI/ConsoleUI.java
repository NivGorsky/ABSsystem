package UI;

import Engine.MainSystem;
import DTO.*;
import Exceptions.XMLFileException;
import com.sun.xml.internal.ws.api.pipe.Engine;

import javax.xml.bind.JAXBException;
import java.lang.System;
import java.util.ArrayList;
import java.util.Scanner;


public class ConsoleUI {
    public enum MainMenu {
        LoadXML, ShowLoansInfo, ShowCustomersInfo, DepositMoney, WithdrawMoney, AssignLoansToLender,
        MoveTimeline, Exit
    }

    private MainSystem engine = new Engine.ABSsystem();
    private MainMenu menu;

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
                "8- Exit");
    }

    public String chooseCustomer()
    {
        int userChoice, i = 0;
        ArrayList<String> customersNames;

        customersNames = (ArrayList<String>) engine.getCustomersNames();
        System.out.println("Please choose a customer:");

        for (String name : customersNames) {
            System.out.println(i + ". " + name);
        }

        userChoice = InputHandler.getCustomer(customersNames.size());
        return customersNames.get(userChoice);
    }

    public String chooseCustomerWithBalance()
    {
        ArrayList<CustomerDTO> customers = engine.showCustomersInfo();
        System.out.println("Please choose a customer:");
        int userChoice, i=0;

        for(CustomerDTO c : customers)
        {
            System.out.println(i + ". name: " + c.getCustomerName() + " balance: " + c.getBalance());
        }

        userChoice = InputHandler.getCustomer(customers.size());
        return customers.get(userChoice).getCustomerName();
    }

    public double chooseAmount(String action) {
        System.out.println("Please enter amount of money you would like to " + action + ":");
        double amount =  InputHandler.getAmount();

        return amount;
    }

    public void runMainMenu() //main menu
    {
        int input;
        boolean isFileLoaded = false;
        MainMenu userChoice = MainMenu.LoadXML; //initialized to something != exit

        while (userChoice != MainMenu.Exit) {
            showMenu();

            input = InputHandler.getOptionFromMenu();
            userChoice = MainMenu.values()[input];
            try {
                switch (userChoice) {
                    case LoadXML: {
                        loadXML();
                        isFileLoaded = true;
                        break;
                    }
                    case ShowLoansInfo: {
                        if (checkFileLoaded(isFileLoaded)) {
                            showLoansInfo();
                            break;
                        }
                    }
                    case ShowCustomersInfo: {
                        if (checkFileLoaded(isFileLoaded)) {
                            showCustomersInfo();
                            break;
                        }
                    }
                    case DepositMoney: {
                        if (checkFileLoaded(isFileLoaded)) {
                            depositMoney();
                            break;
                        }
                    }
                    case WithdrawMoney: {
                        if (checkFileLoaded(isFileLoaded)) {
                            withdrawMoney();
                            break;
                        }
                    }
                    case AssignLoansToLender: {
                        if (checkFileLoaded(isFileLoaded)) {
                            String customer = chooseCustomer();
                            //insert parameters
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
                        System.exit(1);
                    }
                }
            } catch (XMLFileException ex) {
                System.out.println(ex.getExceptionMsg());
            }

        }
    }

    public void loadXML()
    {
        System.out.println("Please enter a path to the XML file (the path will contain english letters only!)");
        String path = InputHandler.getPathToFile();

        try{
            engine.loadXML(path);
        }

        catch (JAXBException e) {
            e.printStackTrace();
         }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void showLoansInfo() {

        ArrayList<LoanDTO> loans = engine.showLoansInfo();

        for (LoanDTO l : loans)
        {
            System.out.println(l.toString());
        }
    }

    public void showCustomersInfo()
    {
        ArrayList<CustomerDTO> customers = engine.showCustomersInfo();

        for(CustomerDTO c : customers)
        {
            System.out.println(c.toString());
        }
    }

    public void depositMoney()
    {
        String customerName = chooseCustomer();
        double amount = chooseAmount("deposit");
        engine.depositMoney(customerName, amount);
        System.out.println("Deposit succeeded!");
    };

    public void withdrawMoney()
    {
        String customerName = chooseCustomer();
        double amount = chooseAmount("withdraw");

        try {
            engine.withdrawMoney(customerName, amount);
            System.out.println("Withdraw succeeded!");
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void assignLoansToLender()
    {
        System.out.println();
    };

    public void moveTimeline()
    {
        TimelineDTO systemTimeline = engine.moveTimeline(); //TODO:return TimelineDTO from engine
        System.out.println("Action succeeded!");
        System.out.println("The previous yaz: " + (systemTimeline.getCurrentYaz() -1) + "The current yaz: " + systemTimeline.getCurrentYaz());
    }

    private boolean checkFileLoaded(boolean isFileLoaded) throws XMLFileException
    {
        if(isFileLoaded == false)
        {
            throw new XMLFileException("XML file not loaded!");
        }

        return true;
    }


}
