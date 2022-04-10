package DTO;

public class AccountMovementDTO {

    private final int yaz;
    private final double amount;
    private final char movementKind;   // +/-/p for payment
    private final double balanceBefore;
    private final double balanceAfter;


    public AccountMovementDTO(int yaz, double amount, char movementKind, double balanceBefore, double balanceAfter) {
        this.yaz = yaz;
        this.amount = amount;
        this.movementKind = movementKind;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString()
    {
        String movement;
        if(movementKind == '+') {
            movement = "deposit";
        }
        else if(movementKind == '-') {
            movement = "withdraw";
        }
        else
        {
            movement = "loan payment";
        }

        return ("Account Movement:\n" +
                "Executed on yaz: " + yaz + "\n" +
                "Amount: " + amount + "\n" +
                "Movement Kind: " + movement + "\n" +
                "The balance before the movement: " + balanceBefore + "\n" +
                "The balance after the movement: " + balanceAfter + "\n");
    }
}