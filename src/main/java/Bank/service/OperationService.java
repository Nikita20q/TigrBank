package Bank.service;

import Bank.domain.factory.OperationFactory;
import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.model.Operation;
import Bank.domain.params.OperationParams;
import Bank.repository.CategoryRepository;
import Bank.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
public class OperationService {
    private OperationRepository operationRepository;
    private OperationFactory operationFactory;

    public OperationService(OperationRepository operationRepository, OperationFactory operationFactory) {
        this.operationRepository = operationRepository;
        this.operationFactory = operationFactory;
    }
    public Operation deposit(BankAccount bankAccount, Category category, BigDecimal amount, String description) {
        bankAccount.deposit(amount);
        OperationParams operationParams = new OperationParams(UUID.randomUUID(), bankAccount.getId(), category.getId(), category.getFlowDirection(), amount, LocalDateTime.now(), description);
        Operation operation = operationFactory.createWithParams(operationParams);
        operationRepository.addOperation(operation);
        return operation;
    }
    public Operation withdraw(BankAccount bankAccount, BigDecimal amount, Category category, String description) {
        if (bankAccount.withdraw(amount)) {
            OperationParams operationParams = new OperationParams(UUID.randomUUID(), bankAccount.getId(), category.getId(), category.getFlowDirection(), amount, LocalDateTime.now(), description);
            Operation operation = operationFactory.createWithParams(operationParams);
            operationRepository.addOperation(operation);
            return operation;
        }
        return null;
    }
    public Operation withdraw(BankAccount bankAccount, BigDecimal amount, Category category) {
        if (bankAccount.withdraw(amount)) {
            OperationParams operationParams = new OperationParams(UUID.randomUUID(), bankAccount.getId(), category.getId(), category.getFlowDirection(), amount, LocalDateTime.now(), "");
            Operation operation = operationFactory.createWithParams(operationParams);
            operationRepository.addOperation(operation);
            return operation;
        }
        return null;
    }

    public boolean deleteById(UUID id) {
        return operationRepository.deleteById(id);
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }
}
