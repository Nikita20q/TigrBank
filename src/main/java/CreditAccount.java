public class CreditAccount extends Account {
    private double creditLimit = 10000;

    CreditAccount(String accountNumber, double creditLimit, Customer owner) {
        super(accountNumber, owner);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (getBalance() - amount >= -creditLimit) {
            setBalance(getBalance() - amount);
            return true;
        }
        return false;
    }
}
