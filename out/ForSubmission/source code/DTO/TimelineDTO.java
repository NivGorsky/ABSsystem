package DTO;

public class TimelineDTO {

    private int currentYaz;

    public TimelineDTO(int systemYaz)
    {
        currentYaz = systemYaz;
    }
    public int getCurrentYaz() {
        return currentYaz;
    }

    @Override
    public String toString()
    {
        String toReturn = "";
        toReturn = ("The previous yaz: " + (currentYaz -1) + "\nThe current yaz: " + currentYaz);

        return toReturn;
    }
}
