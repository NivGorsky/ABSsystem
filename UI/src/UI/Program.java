package UI;

import java.io.*;

public class Program {

    public static void main(String[] args)
    {
        try {
            File f = new File("ABSsystem_Ex01_Tests.log");
            InputStream inputStream = new FileInputStream(f);
            System.setIn(inputStream);
            ConsoleUI systemUI = new ConsoleUI();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        systemUI.runMainMenu();
    }
}
