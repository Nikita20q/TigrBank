package Bank.service;

import Bank.domain.BankAccount;
import Bank.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Service
public class AccountService {
    private final BankAccountRepository bankAccountRepository;
    public AccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount createAccount(String name, BigDecimal balance) {
        BankAccount bankAccount = new BankAccount(UUID.randomUUID(), name, balance);
        bankAccountRepository.add(bankAccount);
        return bankAccount;
    }

    public BankAccount findById(UUID id) {
        return bankAccountRepository.findById(id);
    }

    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }

    public boolean deleteById(UUID id) {
        if (bankAccountRepository.existsById(id)) {
            bankAccountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
