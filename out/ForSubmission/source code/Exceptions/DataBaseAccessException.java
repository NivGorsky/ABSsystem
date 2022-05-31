package Exceptions;

public class DataBaseAccessException extends RuntimeException{

    private final Object dataBase;
    private final String EXCEPTION_MESSAGE;

    public DataBaseAccessException(Object dataBase, String message){
        EXCEPTION_MESSAGE = String.format("Data base access exception - data base caused exception: {0}, \n message: {1}", dataBase, message);
        this.dataBase = dataBase;
    }

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
