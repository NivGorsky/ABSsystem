package UI;

import java.io.*;

public class Program {

    public static void main(String[] args)
    {
        try {
            File f = new File("test2.txt");
            InputStream inputStream = new FileInputStream(f);
            System.setIn(inputStream);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        ConsoleUI systemUI = new ConsoleUI();
        systemUI.runMainMenu();
    }
}
