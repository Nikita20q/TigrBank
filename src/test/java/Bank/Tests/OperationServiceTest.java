package Bank.Tests;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;
import Bank.domain.enums.FlowDirection;
import Bank.repository.OperationRepository;
import Bank.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OperationServiceTest {
    private OperationRepository mockOpRepo;
    private OperationService operationService;

    @BeforeEach
    public void setup() {
        mockOpRepo = Mockito.mock(OperationRepository.class);
        operationService = new OperationService(mockOpRepo);
    }

    @Test
    public void shouldSuccessfullyDeposit() {
        BankAccount account = new BankAccount(UUID.randomUUID(), "Пользователь", new BigDecimal("100"));
        Category category = new Category(UUID.randomUUID(), "Зарплата", FlowDirection.INCOME);
        BigDecimal amount = new BigDecimal("50");

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

        doNothing().when(mockOpRepo).addOperation(any(Operation.class));

        boolean success = operationService.withdraw(account, amount, category, "Обед");

        assertTrue(success);
        assertEquals(new BigDecimal("60"), account.getBalance());
        verify(mockOpRepo, times(1)).addOperation(any(Operation.class));
    }

    @Test
    public void shouldFailWithdrawWithInsufficientFunds() {
        BankAccount account = new BankAccount(UUID.randomUUID(), "Пользователь", new BigDecimal("50"));
        Category category = new Category(UUID.randomUUID(), "Дорогой магазин", FlowDirection.OUTCOME);
        BigDecimal amount = new BigDecimal("100");

        doNothing().when(mockOpRepo).addOperation(any(Operation.class));

        boolean success = operationService.withdraw(account, amount, category, "Неудача");

        assertFalse(success);
        assertEquals(new BigDecimal("50"), account.getBalance(), "Баланс не должен измениться при неудачном снятии");

        verify(mockOpRepo, never()).addOperation(any(Operation.class));
    }

    @Test
    public void shouldAllowWithdrawToZeroBalance() {
        BankAccount account = new BankAccount(UUID.randomUUID(), "Пользователь", new BigDecimal("100"));
        Category category = new Category(UUID.randomUUID(), "Всё под ноль", FlowDirection.OUTCOME);
        BigDecimal amount = new BigDecimal("100");

        doNothing().when(mockOpRepo).addOperation(any(Operation.class));

        boolean success = operationService.withdraw(account, amount, category, "Очистить счёт");

        assertTrue(success);
        assertEquals(BigDecimal.ZERO, account.getBalance());
        verify(mockOpRepo, times(1)).addOperation(any(Operation.class));
    }
}