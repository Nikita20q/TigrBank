package Bank.service;

import Bank.domain.BankAccount;
import Bank.domain.enums.FlowDirection;
import Bank.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class BalanceRecalculationService {
    private OperationRepository operationRepository;
    public BalanceRecalculationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }
    public boolean checkBalance(BankAccount bankAccount) {
        BigDecimal in = operationRepository.sumByType(FlowDirection.INCOME);
        BigDecimal out = operationRepository.sumByType(FlowDirection.OUTCOME);
        if (in.compareTo(out) >= 0) {
            return true;
        }
        return false;
    }
}
