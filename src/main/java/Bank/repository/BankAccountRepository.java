package Bank.repository;

import Bank.domain.BankAccount;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class BankAccountRepository {
    private List<BankAccount> bankAccounts = new ArrayList<>();
    public void add(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
    }
    public List<BankAccount> getBankAccounts() {
        return new ArrayList<>(bankAccounts);
    }

    public BankAccount findById(UUID id) {
        for (BankAccount account : bankAccounts) {
            if (account.getId().equals(id)) {
                return account;
            }
        }
        return null;
    }

    public List<BankAccount> findAll() {
        return getBankAccounts();
    }

    public boolean existsById(UUID id) {
        return findById(id) != null;
    }

    public boolean deleteById(UUID id) {
        return bankAccounts.removeIf(account -> account.getId().equals(id));
    }
}
