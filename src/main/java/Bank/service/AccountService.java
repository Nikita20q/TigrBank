package Bank.service;

import Bank.domain.factory.AccountFactory;
import Bank.domain.model.BankAccount;
import Bank.domain.params.BankAccountParams;
import Bank.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Service
public class AccountService {
    private final BankAccountRepository bankAccountRepository;
    private final AccountFactory accountFactory;
    public AccountService(BankAccountRepository bankAccountRepository, AccountFactory accountFactory) {
        this.bankAccountRepository = bankAccountRepository;
        this.accountFactory = accountFactory;
    }

    public BankAccount createAccount(String name, BigDecimal balance) {
        BankAccountParams entityParams = new BankAccountParams(UUID.randomUUID(), name, balance);
        BankAccount bankAccount = accountFactory.createWithParams(entityParams);
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
