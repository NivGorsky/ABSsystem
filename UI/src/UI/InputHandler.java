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

    public static String getPathToFile()
    {
        Scanner sc = new Scanner(System.in);
        String path = sc.toString();

        try {
            checkPath(path);
        }

        catch(Exception ex) {
            System.out.println(ex.getMessage());
            getPathToFile();
        }

        return path;
    }

    private static void checkPath(String path) throws Exception
    {
        if(path == null)
        {
            throw new Exception("Non path received, please try again");
        }

        else if(!path.matches("[a-zA-Z]+"))
        {
            throw new Exception("The path contains non-English characters! \nplease try again");
        }
    }




}
