package Exceptions;

public class ValueOutOfRangeException extends Exception{

    private double maxValue;
    private double minValue;

    public ValueOutOfRangeException(double minValue, double maxValue, String msg)
    {

        super(String.format(msg+ "\nPlease choose a number between " + minValue + " to " + maxValue));

        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public ValueOutOfRangeException(double minValue, double maxValue)
    {
        super(String.format("Please choose a number between " + minValue +  " to " + maxValue));

        this.maxValue = maxValue;
        this.minValue = minValue;
    }

}
