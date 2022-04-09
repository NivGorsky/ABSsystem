package Engine;

import java.util.Map;

public interface SystemService {

    public Customer getCustomerByName(String name);
    public void moveMoneyBetweenAccounts(Account accountToSubtract, Account accountToAdd, double amount);
    public Map<String, Customer> getAllCustomers();
    public Timeline getTimeLine();

}
