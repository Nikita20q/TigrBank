package Bank.service;

import Bank.domain.model.BankAccount;
import Bank.domain.enums.FlowDirection;
import Bank.domain.model.Operation;
import Bank.repository.BankAccountRepository;
import Bank.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BalanceRecalculationService {

    private final OperationRepository operationRepository;
    private final BankAccountRepository accountRepository;

    public BalanceRecalculationService(OperationRepository operationRepository,
                                       BankAccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
    }

    public boolean checkBalance(BankAccount bankAccount) {
        if (bankAccount == null) {
            return false;
        }

        List<Operation> accountOperations = operationRepository.findAll().stream()
                .filter(op -> op.getBankAccountId().equals(bankAccount.getId()))
                .toList();

        BigDecimal calculatedBalance = BigDecimal.ZERO;

        for (Operation op : accountOperations) {
            if (op.getFlowDirection() == FlowDirection.INCOME) {
                calculatedBalance = calculatedBalance.add(op.getAmount());
            } else {
                calculatedBalance = calculatedBalance.subtract(op.getAmount());
            }
        }

        boolean isCorrect = bankAccount.getBalance().compareTo(calculatedBalance) == 0;

        if (!isCorrect) {
            System.out.println("Баланс некорректен для счёта: " + bankAccount.getName());
            System.out.println("Ожидалось: " + calculatedBalance + " RUB");
            System.out.println("Фактически: " + bankAccount.getBalance() + " RUB");
        } else {
            System.out.println("Баланс счёта корректен: " + bankAccount.getBalance() + " RUB");
        }

        return isCorrect;
    }

    public void recalculate(UUID userId) {
        BankAccount bankAccount = accountRepository.findById(userId);
        if (bankAccount == null) {
            throw new IllegalArgumentException("Счёт не может быть null");
        }

        List<Operation> accountOperations = operationRepository.findAll().stream()
                .filter(op -> op.getBankAccountId().equals(bankAccount.getId()))
                .toList();

        BigDecimal newBalance = BigDecimal.ZERO;

        for (Operation op : accountOperations) {
            if (op.getFlowDirection() == FlowDirection.INCOME) {
                newBalance = newBalance.add(op.getAmount());
            } else {
                newBalance = newBalance.subtract(op.getAmount());
            }
        }

        bankAccount.setBalance(newBalance);

        System.out.println("Баланс пересчитан для счёта: " + bankAccount.getName());
        System.out.println("Новый баланс: " + newBalance + " RUB");
    }

}
