package bank.tests;

import Bank.domain.Command.DepositCommand;
import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.model.Operation;
import Bank.domain.enums.FlowDirection;
import Bank.service.AccountService;
import Bank.service.BalanceRecalculationService;
import Bank.service.CategoryService;
import Bank.service.OperationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DepositCommandTest {

    private AccountService mockAccount;
    private OperationService mockOperation;
    private CategoryService mockCategory;
    private BalanceRecalculationService mockRecalc;

    @BeforeEach
    public void setup() {
        mockAccount = Mockito.mock(AccountService.class);
        mockOperation = Mockito.mock(OperationService.class);
        mockCategory = Mockito.mock(CategoryService.class);
        mockRecalc = Mockito.mock(BalanceRecalculationService.class);
    }

    @Test
    public void shouldExecuteDepositSuccessfully() {
        String accountId = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal("500");
        String categoryName = "Зарплата";

        BankAccount account = new BankAccount(UUID.fromString(accountId), "Тест", new BigDecimal("1000"));
        Category category = new Category(UUID.randomUUID(), categoryName, FlowDirection.INCOME);
        Operation operation = new Operation(
                UUID.randomUUID(),
                account.getId(),
                category.getId(),
                FlowDirection.INCOME,
                amount,
                LocalDateTime.now(),
                "Пополнение"
        );

        when(mockAccount.findById(UUID.fromString(accountId))).thenReturn(account);
        when(mockCategory.findByNameAndType(eq(categoryName), eq(FlowDirection.INCOME))).thenReturn(category);
        when(mockOperation.deposit(eq(account), eq(category), eq(amount), any())).thenReturn(operation);

        DepositCommand cmd = new DepositCommand(
                mockAccount, mockOperation, mockCategory, mockRecalc,
                accountId, amount, categoryName
        );

        cmd.execute();

        verify(mockAccount).findById(UUID.fromString(accountId));
        verify(mockCategory).findByNameAndType(eq(categoryName), eq(FlowDirection.INCOME));
        verify(mockOperation).deposit(eq(account), eq(category), eq(amount), any());
        assertEquals("Deposit", cmd.getName());
    }

    @Test
    public void shouldHandleInvalidAccountId() {
        String invalidId = "not-a-uuid";

        DepositCommand cmd = new DepositCommand(
                mockAccount, mockOperation, mockCategory, mockRecalc,
                invalidId, new BigDecimal("100"), "Категория"
        );

        assertDoesNotThrow(() -> cmd.execute());
        verify(mockAccount, never()).findById(any());
    }

    @Test
    public void shouldReturnCorrectName() {
        DepositCommand cmd = new DepositCommand(
                mockAccount, mockOperation, mockCategory, mockRecalc,
                UUID.randomUUID().toString(), BigDecimal.ZERO, "Тест"
        );

        assertEquals("Deposit", cmd.getName());
    }
}