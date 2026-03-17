package Bank.service;

import Bank.domain.model.Category;
import Bank.domain.model.Operation;
import Bank.domain.enums.FlowDirection;
import Bank.repository.CategoryRepository;
import Bank.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {
    private OperationRepository operationRepository;
    private CategoryRepository categoryRepository;
    public AnalyticsService(OperationRepository operationRepository,  CategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.categoryRepository = categoryRepository;
    }
    public BigDecimal getNetIncome(LocalDateTime from, LocalDateTime to) {
        BigDecimal income = operationRepository.sumByTypeAndDate(FlowDirection.INCOME, from, to);
        BigDecimal outcome = operationRepository.sumByTypeAndDate(FlowDirection.OUTCOME, from, to);
        return income.subtract(outcome);
    }

    public Map<Category, BigDecimal> getIncomesByCategory(LocalDateTime from, LocalDateTime to) {
        return groupOperationByCategory(FlowDirection.INCOME, from, to);
    }

    public Map<Category, BigDecimal> getExpensesByCategory(LocalDateTime from, LocalDateTime to) {
        return groupOperationByCategory(FlowDirection.OUTCOME, from, to);
    }

    public Map<Category, BigDecimal> groupOperationByCategory(FlowDirection flowDirection, LocalDateTime from, LocalDateTime to) {
        Map<Category, BigDecimal> map = new HashMap<>();

        for (Operation operation : operationRepository.getAllOperations()) {

            if (operation.getFlowDirection() != flowDirection) {
                continue;
            }

            LocalDateTime opDate = operation.getDate();
            if (opDate.isBefore(from) || opDate.isAfter(to)) {
                continue;
            }


            Category category = categoryRepository.findById(operation.getCategoryId());

            if (category == null) {
                continue;
            }

            BigDecimal currentSum = map.getOrDefault(category, BigDecimal.ZERO);
            BigDecimal newSum = currentSum.add(operation.getAmount());
            map.put(category, newSum);
        }
        return map;
    }
}
