package UI;

import Engine.ABSsystem;
import Exceptions.ValueOutOfRangeException;

import java.util.Scanner;

public class InputHandler {

    public static int getOptionFromMenu()
    {
        int input;
        Scanner sc = new Scanner(System.in);
        input  = sc.nextInt();

        try {
            checkOptionFromMenu(input);
        }
        catch (ValueOutOfRangeException ex) {
            System.out.println(ex.getMessage());
            getOptionFromMenu();
        }

        return input;
    }

    private static void checkOptionFromMenu(int choice) throws ValueOutOfRangeException
    {
        int numberOfOptions  = ConsoleUI.MainMenu.values().length;
        if((choice > numberOfOptions) || (choice <=0))
        {
            throw new ValueOutOfRangeException(1, 8);
        }
    }

    public static int getCustomer(int numOfCustomers)
    {
        int userChoice;
        Scanner sc = new Scanner(System.in);
        userChoice = sc.nextInt();

    try {
        checkCustomer(userChoice, numOfCustomers);
    }
    catch (ValueOutOfRangeException ex) {
        System.out.println(ex.getMessage());
        getCustomer(numOfCustomers);
    }

        return userChoice;
    }

    private static void checkCustomer(int userChoice, int numOfCustomers) throws ValueOutOfRangeException
    {
        if(userChoice > numOfCustomers || userChoice <= 0)
        {
            throw new ValueOutOfRangeException(1, numOfCustomers);
        }
    }

    public static double getAmount()
    {
        double amount;

        Scanner sc = new Scanner(System.in);
        amount = sc.nextDouble();

        return amount;
    }




}
