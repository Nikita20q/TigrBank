package bank.tests;

import Bank.domain.Command.WithdrawCommand;
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

public class WithdrawCommandTest {

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
    public void shouldExecuteWithdrawSuccessfully() {
        String accountId = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal("200");
        String categoryName = "Кафе";

        BankAccount account = new BankAccount(UUID.fromString(accountId), "Тест", new BigDecimal("1000"));
        Category category = new Category(UUID.randomUUID(), categoryName, FlowDirection.OUTCOME);
        Operation operation = new Operation(
                UUID.randomUUID(),
                account.getId(),
                category.getId(),
                FlowDirection.OUTCOME,
                amount,
                LocalDateTime.now(),
                "Расход"
        );

        when(mockAccount.findById(UUID.fromString(accountId))).thenReturn(account);
        when(mockCategory.findByNameAndType(eq(categoryName), eq(FlowDirection.OUTCOME))).thenReturn(category);
        when(mockOperation.withdraw(eq(account), eq(amount), eq(category), any())).thenReturn(operation);

        WithdrawCommand cmd = new WithdrawCommand(
                mockAccount, mockOperation, mockCategory, mockRecalc,
                accountId, amount, categoryName
        );

        cmd.execute();

        verify(mockAccount).findById(UUID.fromString(accountId));
        verify(mockCategory).findByNameAndType(eq(categoryName), eq(FlowDirection.OUTCOME));
        verify(mockOperation).withdraw(eq(account), eq(amount), eq(category), any());
        assertEquals("Withdraw", cmd.getName());
    }

    @Test
    public void shouldHandleInsufficientFunds() {
        String accountId = UUID.randomUUID().toString();
        BigDecimal amount = new BigDecimal("5000");
        String categoryName = "Дорогая покупка";

        BankAccount account = new BankAccount(UUID.fromString(accountId), "Тест", new BigDecimal("100"));
        Category category = new Category(UUID.randomUUID(), categoryName, FlowDirection.OUTCOME);

        when(mockAccount.findById(UUID.fromString(accountId))).thenReturn(account);
        when(mockCategory.findByNameAndType(eq(categoryName), eq(FlowDirection.OUTCOME))).thenReturn(category);
        when(mockOperation.withdraw(eq(account), eq(amount), eq(category), any())).thenReturn(null);

        WithdrawCommand cmd = new WithdrawCommand(
                mockAccount, mockOperation, mockCategory, mockRecalc,
                accountId, amount, categoryName
        );

        cmd.execute();

        verify(mockOperation).withdraw(eq(account), eq(amount), eq(category), any());
    }

    @Test
    public void shouldReturnCorrectName() {
        WithdrawCommand cmd = new WithdrawCommand(
                mockAccount, mockOperation, mockCategory, mockRecalc,
                UUID.randomUUID().toString(), BigDecimal.ZERO, "Тест"
        );

        assertEquals("Withdraw", cmd.getName());
    }
}