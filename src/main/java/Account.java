public class Account {
    private String accountNumber;
    private double balance;
    private Customer owner;

    public Account(String accountNumber, Customer owner) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
    }

    public boolean deposit(double amount) {
        balance += amount;
        return true;
    }
    public boolean withdraw(double amount) {
        if (balance - amount >= 0) {
            balance -= amount;
            return true;
        }
        return false;
    }
    public boolean transfer(Account to, double amount) {
        if (balance - amount >= 0) {
            balance -= amount;
            to.balance += amount;
            return true;
        }
        return false;
    }

    public Customer getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}