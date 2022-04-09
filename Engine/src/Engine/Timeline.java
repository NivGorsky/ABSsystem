package Engine;

public class Timeline {

    private int currentYaz = 1;

    public int getCurrentYaz() { return currentYaz; }
    public void moveTimeline (int yaz)
    {
        currentYaz += yaz;
    }
    public void resetSystemYaz() { currentYaz = 1; }
}
