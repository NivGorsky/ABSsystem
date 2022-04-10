package Exceptions;

public class XMLFileException extends Exception{

    private String ExceptionMsg = new String();

    public XMLFileException(String msg)
    {
        this.ExceptionMsg = msg;
    }

    public String getExceptionMsg() {
        return ExceptionMsg;
    }
}
