package Exceptions;

public class ValueOutOfRangeException extends Exception{

    private double maxValue;
    private double minValue;

    public ValueOutOfRangeException(double minValue, double maxValue, String msg)
    {

        super(String.format("{0}\nPlease choose a number between {1} to {2}", msg, minValue, maxValue));

        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public ValueOutOfRangeException(double minValue, double maxValue)
    //TODO:check if i need to print integers (casting/Object field)
    {
        super(String.format("Please choose a number between {0} to {1}", minValue, maxValue));

        this.maxValue = maxValue;
        this.minValue = minValue;
    }

}
