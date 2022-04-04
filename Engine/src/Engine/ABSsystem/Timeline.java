package Engine.ABSsystem;

public class Timeline {

    private int currentYaz = 1;

    public int getCurrentYaz() { return currentYaz; }
    public void moveTimeline ()
    {
        currentYaz++;
    }
}
