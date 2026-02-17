package Bank.Tests;

import Bank.domain.Category;
import Bank.domain.Operation;
import Bank.domain.enums.FlowDirection;
import Bank.repository.CategoryRepository;
import Bank.repository.OperationRepository;
import Bank.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AnalyticServiceTest {

    private OperationRepository mockOpRepo;
    private CategoryRepository mockCatRepo;
    private AnalyticsService analyticsService;

    @BeforeEach
    public void setup() {
        mockOpRepo = Mockito.mock(OperationRepository.class);
        mockCatRepo = Mockito.mock(CategoryRepository.class);
        analyticsService = new AnalyticsService(mockOpRepo, mockCatRepo);
    }

    @Test
    public void shouldCalculatePositiveNetIncome() {
        LocalDateTime start = LocalDate.of(2026, 2, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(2026, 2, 28).atTime(23, 59, 59);

        when(mockOpRepo.sumByTypeAndDate(eq(FlowDirection.INCOME), eq(start), eq(end)))
                .thenReturn(new BigDecimal("10000"));
        when(mockOpRepo.sumByTypeAndDate(eq(FlowDirection.OUTCOME), eq(start), eq(end)))
                .thenReturn(new BigDecimal("3000"));

        BigDecimal result = analyticsService.getNetIncome(start, end);

        assertEquals(0, new BigDecimal("7000").compareTo(result), "Чистый доход должен быть 7000");
    }

    @Test
    public void shouldGroupExpensesByCategoryCorrectly() {
        LocalDateTime start = LocalDate.of(2026, 2, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(2026, 2, 28).atTime(23, 59, 59);

        Category catCafe = new Category(UUID.randomUUID(), "Кафе", FlowDirection.OUTCOME);
        Category catTransport = new Category(UUID.randomUUID(), "Транспорт", FlowDirection.OUTCOME);
        Category catSalary = new Category(UUID.randomUUID(), "Зарплата", FlowDirection.INCOME); // Лишняя, не должна попасть в отчет расходов

        Operation op1 = new Operation(UUID.randomUUID(), UUID.randomUUID(), catCafe.getId(), FlowDirection.OUTCOME, new BigDecimal("500"), start.plusDays(1), "Обед");
        Operation op2 = new Operation(UUID.randomUUID(), UUID.randomUUID(), catCafe.getId(), FlowDirection.OUTCOME, new BigDecimal("300"), start.plusDays(2), "Ужин");
        Operation op3 = new Operation(UUID.randomUUID(), UUID.randomUUID(), catTransport.getId(), FlowDirection.OUTCOME, new BigDecimal("100"), start.plusDays(3), "Автобус");
        Operation op4 = new Operation(UUID.randomUUID(), UUID.randomUUID(), catSalary.getId(), FlowDirection.INCOME, new BigDecimal("50000"), start.plusDays(1), "ЗП"); // Не должна учитываться
        Operation op5 = new Operation(UUID.randomUUID(), UUID.randomUUID(), catCafe.getId(), FlowDirection.OUTCOME, new BigDecimal("200"), end.plusDays(1), "За пределами периода"); // Не должна учитываться (дата вне периода)

        List<Operation> allOperations = new ArrayList<>();
        allOperations.add(op1);
        allOperations.add(op2);
        allOperations.add(op3);
        allOperations.add(op4);
        allOperations.add(op5);

        when(mockOpRepo.getAllOperations()).thenReturn(allOperations);

        when(mockCatRepo.findById(catCafe.getId())).thenReturn(catCafe);
        when(mockCatRepo.findById(catTransport.getId())).thenReturn(catTransport);
        when(mockCatRepo.findById(catSalary.getId())).thenReturn(catSalary);

        Map<Category, BigDecimal> result = analyticsService.getExpensesByCategory(start, end);

        assertEquals(2, result.size(), "Должно быть 2 категории расходов (Кафе и Транспорт)");

        assertTrue(result.containsKey(catCafe));
        assertEquals(0, new BigDecimal("800").compareTo(result.get(catCafe)), "Сумма расходов на Кафе должна быть 800");

        assertTrue(result.containsKey(catTransport));
        assertEquals(0, new BigDecimal("100").compareTo(result.get(catTransport)), "Сумма расходов на Транспорт должна быть 100");

        assertTrue(!result.containsKey(catSalary), "Доходы не должны попадать в отчет расходов");
    }

    @Test
    public void shouldReturnEmptyMapWhenNoOperations() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        when(mockOpRepo.getAllOperations()).thenReturn(new ArrayList<>());

        Map<Category, BigDecimal> result = analyticsService.getIncomesByCategory(start, end);

        assertTrue(result.isEmpty(), "Карта должна быть пустой, если операций нет");
    }
}