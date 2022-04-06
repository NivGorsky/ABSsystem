package Engine;

public interface SystemService {

    public Customer getCustomerByName(String name);
    public void moveMoneyBetweenAccounts(Account accountToSubtract, Account accountToAdd, double amount);


}
