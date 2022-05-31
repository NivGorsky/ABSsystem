package Exceptions;

public class SystemRestrictionsException extends Exception{
    private final Object thrower;
    private final String EXCEPTION_MESSAGE;

    public SystemRestrictionsException(Object thrower, String message){
        EXCEPTION_MESSAGE = String.format("System restrictions - " + message);
        this.thrower = thrower;
    }

    @Override
    public String getMessage() {
        return EXCEPTION_MESSAGE;
    }
}
