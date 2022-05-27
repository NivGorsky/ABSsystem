package Exceptions;

public class XMLFileException extends Exception{

    private String ExceptionMsg = new String();

    public XMLFileException(String msg)
    {
        this.ExceptionMsg = msg;
    }

    @Override
    public String getMessage() {
        return ExceptionMsg;
    }
}
