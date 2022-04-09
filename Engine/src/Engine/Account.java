package Engine;

import java.util.ArrayList;

public class Account {

    public class AccountMovement{
        private final int yaz;
        private final double amount;
        private final char movementKind;   // +/-
        private final double balanceBefore;
        private final double balanceAfter;

        public AccountMovement(int yaz, double amount, char movementKind) {
            this.yaz = yaz;
            this.amount = amount;
            this.movementKind = movementKind;
            this.balanceBefore = balance;
            this.balanceAfter = balance + amount;
        }

        public int getYaz() { return yaz; }
        public double getAmount() { return amount; }
        public char getMovementKind() { return movementKind; }
        public double getBalanceBefore() { return balanceBefore; }
        public double getBalanceAfter() { return balanceAfter; }
    }

    private double balance;
    private ArrayList<AccountMovement> movements;

    public Account(double balance)
    {
        this.balance = balance;
        this.movements = new ArrayList<AccountMovement>();
    }

    public double getBalance() { return balance; }
    public ArrayList<AccountMovement> getMovements() { return movements; }

    public void addToBalance(int yaz, double amount)
    {
        movements.add(new AccountMovement(yaz, amount, '+'));
        balance += amount;
    }
    public void substructFromBalance(int yaz, double amount)
    {
        movements.add(new AccountMovement(yaz, amount, '-'));
        balance -= amount;
    }



}
