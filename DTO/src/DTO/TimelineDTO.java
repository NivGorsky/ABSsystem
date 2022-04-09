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
}
