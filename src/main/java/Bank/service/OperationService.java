package Bank.service;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;
import Bank.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
public class OperationService {
    private OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }
    public void deposit(BankAccount bankAccount, Category category, BigDecimal amount, String description) {
        bankAccount.deposit(amount);
        Operation operation = new Operation(UUID.randomUUID(), bankAccount.getId(), category.getId(), category.getFlowDirection(), amount, LocalDateTime.now(), description);
        operationRepository.addOperation(operation);
    }
    public boolean withdraw(BankAccount bankAccount, BigDecimal amount, Category category, String description) {
        if (bankAccount.withdraw(amount)) {
            Operation operation = new Operation(UUID.randomUUID(), bankAccount.getId(), category.getId(), category.getFlowDirection(), amount, LocalDateTime.now(), description);
            operationRepository.addOperation(operation);
            return true;
        }
        return false;
    }
    public boolean withdraw(BankAccount bankAccount, BigDecimal amount, Category category) {
        if (bankAccount.withdraw(amount)) {
            Operation operation = new Operation(UUID.randomUUID(), bankAccount.getId(), category.getId(), category.getFlowDirection(), amount, LocalDateTime.now(), "");
            operationRepository.addOperation(operation);
            return true;
        }
        return false;
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }
}
