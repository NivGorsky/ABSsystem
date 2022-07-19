package Engine;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileWriter;

import static jsonDeserializer.GsonWrapper.GSON;

public class Configurations{
    public static FileWriter myWriter = null;
    public static void closeStream(){try{myWriter.close();} catch (Exception e){};}


    public final static void printToFile(String objectToPrint){

        if(myWriter == null){
            try{
                myWriter = new FileWriter("/Users/nivos/projects/ABSsystem/logFileEngine.txt");
            }

            catch (Exception e){

            }
        }

        try{
            myWriter.write(objectToPrint);
        }

        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    }

