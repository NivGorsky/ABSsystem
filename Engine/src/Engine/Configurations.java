package Engine;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.PrintWriter;

import static jsonDeserializer.GsonWrapper.GSON;

public class Configurations{
    public static PrintWriter logFileWriter = null;
    public static void closeStream(){try{logFileWriter.close();} catch (Exception e){};}

    public final static void printToFile(String objectToPrint){

        if(logFileWriter == null){
            try{
                logFileWriter = new PrintWriter("/Users/nivos/projects/ABSsystem/engineLogFile.txt");
            }

            catch (Exception e){
                System.out.println("could not write to file customer");

            }
        }

        try{
            logFileWriter.println(objectToPrint);
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}


