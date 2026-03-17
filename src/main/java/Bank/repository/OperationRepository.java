package Bank.repository;

import Bank.domain.model.Operation;
import Bank.domain.enums.FlowDirection;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class OperationRepository {
    private final List<Operation> operationRepository = new ArrayList<>();
    public List<Operation> getAllOperations() {
        return new ArrayList<>(operationRepository);
    }
    public void addOperation(Operation operation) {
        operationRepository.add(operation);
    }

    public BigDecimal sumByTypeAndDate(FlowDirection type, LocalDateTime from, LocalDateTime to) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Operation operation : operationRepository) {
            LocalDateTime opDate = operation.getDate();
            if (!opDate.isBefore(from) && !opDate.isAfter(to)) {
                if (operation.getFlowDirection() == type) {
                    sum = sum.add(operation.getAmount());
                }
            }
        }
        return sum;
    }

    public BigDecimal sumByType(FlowDirection type) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Operation operation : operationRepository) {
            if (operation.getFlowDirection() == type) {
                sum = sum.add(operation.getAmount());
            }
        }
        return sum;
    }

    public List<Operation> getOperations() {
        return new ArrayList<>(operationRepository);
    }

    public List<Operation> findAll() {
        return getOperations();
    }

    public Operation findById(UUID id) {
        return operationRepository.stream()
                .filter(op -> op.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteById(UUID id) {
        Operation operation = findById(id);
        if (operation != null) {
            return operationRepository.remove(operation);
        }
        return false;
    }
}
