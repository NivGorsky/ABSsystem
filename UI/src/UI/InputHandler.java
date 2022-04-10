package UI;

import Engine.XML_Handler.XMLFileChecker;
import Exceptions.ValueOutOfRangeException;
import Exceptions.XMLFileException;
import java.util.ArrayList;
import java.util.Scanner;

public class InputHandler {

    private final Scanner sc;

    public InputHandler(){
        sc = new Scanner(System.in);
    }

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
        if(userChoice > numOfCustomers || userChoice < 0)
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
        Scanner sc = new Scanner(System.in);
        ArrayList<String> categories = new ArrayList<String>();

        for (int i=1;i<=numberOfCategoriesToChoose; ++i){
            System.out.println("Category " + i + ":");
            String category = sc.nextLine();
            while(!isCategorySupported(category, supportedCategories)){
                category = sc.toString();
            }
            categories.add(sc.toString());
        }

        return categories;
    }

    public static int getPositiveNumber()
    {
        Scanner sc = new Scanner(System.in);
        int res = sc.nextInt();

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

    public static double getMinInterestPerYaz(){
        Scanner sc = new Scanner(System.in);
        double minInterest = sc.nextDouble();

        while(!isMinInterestSupported(minInterest)){
            minInterest = sc.nextDouble();
        }

        return minInterest;
    }

    private static boolean isMinInterestSupported(double minInterest){
        return minInterest > 0 || minInterest == -1;
    }

    public static int getMinYazForReturn(){
        Scanner sc = new Scanner(System.in);

        int res = sc.nextInt();

        while(res < 0 && res != -1){
            System.out.println("Please choose a valid value");
            res = sc.nextInt();
        }

        return res;
    }
}
