package UI;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Program {

    public static void main(String[] args)
    {
        try {
            FileInputStream stream = new FileInputStream("ABSsystem_Ex01_Tests.log");
            System.setIn(stream);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        ConsoleUI systemUI = new ConsoleUI();
        systemUI.runMainMenu();
    }
}
