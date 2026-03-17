package bank.tests;

import Bank.domain.factory.OperationFactory;
import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.model.Operation;
import Bank.domain.enums.FlowDirection;
import Bank.domain.params.OperationParams;
import Bank.repository.OperationRepository;
import Bank.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OperationServiceTest {
    private OperationRepository mockOpRepo;
    private OperationService operationService;
    private OperationFactory mockFactory;

    @BeforeEach
    public void setup() {
        mockOpRepo = Mockito.mock(OperationRepository.class);
        mockFactory = Mockito.mock(OperationFactory.class);
        operationService = new OperationService(mockOpRepo, mockFactory);
    }

    @Test
    public void shouldSuccessfullyDeposit() {
        BankAccount account = new BankAccount(UUID.randomUUID(), "Пользователь", new BigDecimal("100"));
        Category category = new Category(UUID.randomUUID(), "Зарплата", FlowDirection.INCOME);
        BigDecimal amount = new BigDecimal("50");

        Operation expectedOperation = new Operation(
                UUID.randomUUID(),
                account.getId(),
                category.getId(),
                FlowDirection.INCOME,
                amount,
                LocalDateTime.now(),
                "Тестовое пополнение"
        );

        when(mockFactory.createWithParams(any(OperationParams.class))).thenReturn(expectedOperation);
        doNothing().when(mockOpRepo).addOperation(any(Operation.class));

        operationService.deposit(account, category, amount, "Тестовое пополнение");

        assertEquals(new BigDecimal("150"), account.getBalance());

        verify(mockOpRepo, times(1)).addOperation(any(Operation.class));
    }

    @Test
    public void shouldSuccessfullyWithdrawWithSufficientFunds() {
        BankAccount account = new BankAccount(UUID.randomUUID(), "Пользователь", new BigDecimal("100"));
        Category category = new Category(UUID.randomUUID(), "Кафе", FlowDirection.OUTCOME);
        BigDecimal amount = new BigDecimal("40");

        Operation expectedOperation = new Operation(
                UUID.randomUUID(),
                account.getId(),
                category.getId(),
                FlowDirection.OUTCOME,
                amount,
                LocalDateTime.now(),
                "Обед"
        );

        when(mockFactory.createWithParams(any(OperationParams.class))).thenReturn(expectedOperation);
        doNothing().when(mockOpRepo).addOperation(any(Operation.class));

        Operation operation = operationService.withdraw(account, amount, category, "Обед");

        assertNotNull(operation);
        assertEquals(new BigDecimal("60"), account.getBalance());
        verify(mockOpRepo, times(1)).addOperation(any(Operation.class));
    }

    @Test
    public void shouldFailWithdrawWithInsufficientFunds() {
        BankAccount account = new BankAccount(UUID.randomUUID(), "Пользователь", new BigDecimal("50"));
        Category category = new Category(UUID.randomUUID(), "Дорогой магазин", FlowDirection.OUTCOME);
        BigDecimal amount = new BigDecimal("100");

        doNothing().when(mockOpRepo).addOperation(any(Operation.class));

        Operation operation = operationService.withdraw(account, amount, category, "Неудача");

        assertNull(operation);
        assertEquals(new BigDecimal("50"), account.getBalance(), "Баланс не должен измениться при неудачном снятии");

        verify(mockOpRepo, never()).addOperation(any(Operation.class));
    }

    @Test
    public void shouldAllowWithdrawToZeroBalance() {
        BankAccount account = new BankAccount(UUID.randomUUID(), "Пользователь", new BigDecimal("100"));
        Category category = new Category(UUID.randomUUID(), "Полный вывод", FlowDirection.OUTCOME);
        BigDecimal amount = new BigDecimal("100");

        Operation expectedOperation = new Operation(
                UUID.randomUUID(),
                account.getId(),
                category.getId(),
                FlowDirection.OUTCOME,
                amount,
                LocalDateTime.now(),
                "Полный вывод"
        );

        when(mockFactory.createWithParams(any(OperationParams.class))).thenReturn(expectedOperation);
        doNothing().when(mockOpRepo).addOperation(any(Operation.class));

        Operation operation = operationService.withdraw(account, amount, category, "Полный вывод");

        assertNotNull(operation);
        assertEquals(BigDecimal.ZERO, account.getBalance());
        verify(mockOpRepo, times(1)).addOperation(any(Operation.class));
    }
}