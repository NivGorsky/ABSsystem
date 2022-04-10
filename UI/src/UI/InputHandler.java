package UI;

import Engine.XML_Handler.XMLFileChecker;
import Exceptions.ValueOutOfRangeException;
import Exceptions.XMLFileException;
import java.util.ArrayList;
import java.util.Scanner;

public class InputHandler {

    private static Scanner sc = new Scanner(System.in);

    public static int getOptionFromMenu()
    {
        int input;
        input = Integer.parseInt(sc.nextLine());

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
        userChoice = Integer.parseInt(sc.nextLine());

    try {
        checkCustomer(userChoice, numOfCustomers);
    }
    catch (ValueOutOfRangeException ex)
    {
        System.out.println(ex.getMessage());
        getCustomer(numOfCustomers);
    }

        return userChoice;
    }

    private static void checkCustomer(int userChoice, int numOfCustomers) throws ValueOutOfRangeException
    {
        if(userChoice > numOfCustomers || userChoice < 0)
        {
            throw new ValueOutOfRangeException(1, numOfCustomers);
        }
    }

    public static double getAmount()
    {
        double amount;

//        Scanner sc = new Scanner(System.in);
//        amount = sc.nextDouble();
        amount = Double.parseDouble(sc.nextLine());

        return amount;
    }

    public static String getPathToFile()
    {
        String path = sc.nextLine();

        try {
            XMLFileChecker.checkPath(path);
        }

        catch(Exception ex) {
            System.out.println(ex.getMessage());
            getPathToFile();
        }

        return path;
    }

    public static ArrayList<String> chooseCategories(int numberOfCategoriesToChoose, ArrayList<String> supportedCategories){
        ArrayList<String> categories = new ArrayList<String>();

        for (int i=1;i<=numberOfCategoriesToChoose; ++i){
            System.out.println("Category " + i + ":");
            String category = sc.nextLine();

            while(!isCategorySupported(category, supportedCategories)){
                System.out.println("Category " + i + ":");
                category = sc.nextLine();
            }
            categories.add(category);
        }

        return categories;
    }

    public static int getPositiveNumber()
    {
        int res = Integer.parseInt(sc.nextLine());

        while(res <= 0){
            res = sc.nextInt();
        }

        return res;
    }

    private static boolean isCategorySupported(String category,ArrayList<String> categories){
        for (String supportedCategory:categories)
        {
            if(category.equals(supportedCategory))
            {
                return true;
            }
        }

        System.out.println("Category is not supported, please choose only supported categories");
        return false;
    }

    public static double getMinInterestPerYaz()
    {
        double minInterest = Double.parseDouble(sc.nextLine());

        while(!isMinInterestSupported(minInterest))
        {
            minInterest = Double.parseDouble(sc.nextLine());
        }

        return minInterest;
    }

    private static boolean isMinInterestSupported(double minInterest){
        return minInterest > 0 || minInterest == -1;
    }

    public static int getMinYazForReturn(){
//        Scanner sc = new Scanner(System.in);

//        int res = sc.nextInt();
        int res = Integer.parseInt(sc.nextLine());

        while(res < 0 && res != -1){
            System.out.println("Please choose a valid value");
            res = Integer.parseInt(sc.nextLine());
        }

        return res;
    }
}
