import java.util.ArrayList;

public class Bank {
    ArrayList<Customer> customers;
    ArrayList<Account> accounts;
    ArrayList<Transaction> transactions;

    public Bank() {
        customers = new ArrayList<>();
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public Customer createCustomer(String fullName) {
        Customer customer = new Customer(customers.size(), fullName);
        customers.add(customer);
        return customer;
    }

    public Account openDebitAccount(Customer owner) {
        if (findCustomer(owner.getId()) != null) {
            Account debitAccount = new DebitAccount(Integer.toString(accounts.size()), owner);
            accounts.add(debitAccount);
            return debitAccount;
        }
        return null;
    }

    public Account openCreditAccount(Customer owner, double creditLimit) {
        if (findCustomer(owner.getId()) != null) {
            Account creditAccount = new CreditAccount(Integer.toString(accounts.size()), creditLimit, owner);
            accounts.add(creditAccount);
            return creditAccount;
        }
        return null;
    }

    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) return account;
        }
        return null;
    }

    public Customer findCustomer(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) return customer;
        }
        return null;
    }

    public boolean deposit(String accountNumber, int amount) {
        Account account = findAccount(accountNumber);
        if (account != null) {
            account.deposit(amount);
            return true;
        }
        return false;
    }

    public boolean withdraw(String accountNumber, int amount) {
        Account account = findAccount(accountNumber);
        if (account != null) {
            return account.withdraw(amount);
        }
        return false;
    }

    public boolean transfer(String from, String to, double amount) {
        if (from == null || to == null) return false;
        Account accountFrom = findAccount(from);
        Account accountTo = findAccount(to);
        if (accountTo != null && accountFrom != null) return accountFrom.transfer(accountTo, amount);
        return false;
    }

    public void printCustomerAccounts(int customerId) {
        for (Account account : accounts) {
            if (account.getOwner().getId() == customerId) System.out.println(account.getOwner().getFullName());
        }
    }

    public void printTransactions() {
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }

    public void printReport() {
        int creditAccounts = 0, debitAccounts = 0;
        int creditBalance = 0, debitBalance = 0;
        int errorOperation = 0, successOperation = 0;
        for (Account account : accounts) {
            if (account instanceof CreditAccount) {
                creditAccounts++;
                creditBalance += account.getBalance();
            }
            if (account instanceof DebitAccount) {
                debitAccounts++;
                debitBalance += account.getBalance();
            }
        }
        for (Transaction transaction : transactions) {
            if (transaction.isSuccess()) successOperation ++;
            else errorOperation++;
        }
        System.out.println("Кол-во аккаунтов\nКредитные: " + creditAccounts + "\nДебитовые: " + debitAccounts);
        System.out.println("Балансы\nКредитные: " + creditBalance + "\nДебитовые: " + debitBalance);
        System.out.println("Кол-во операций\nУспешные: " + successOperation + "\nНеуспешные: " + errorOperation);
    }
}