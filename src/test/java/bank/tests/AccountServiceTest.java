package bank.tests;

import Bank.domain.factory.AccountFactory;
import Bank.domain.model.BankAccount;
import Bank.domain.params.BankAccountParams;
import Bank.repository.BankAccountRepository;
import Bank.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private BankAccountRepository mockAccountRepository;
    private AccountFactory mockFactory;
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        mockAccountRepository = Mockito.mock(BankAccountRepository.class);
        mockFactory = Mockito.mock(AccountFactory.class);
        accountService = new AccountService(mockAccountRepository, mockFactory);
    }

    @Test
    public void createWithZeroBalance() {
        String newName = "Тестовый пользователь";
        BigDecimal balance = BigDecimal.ZERO;

        BankAccount expectedAccount = new BankAccount(
                UUID.randomUUID(),
                newName,
                balance
        );

        when(mockFactory.createWithParams(any(BankAccountParams.class))).thenReturn(expectedAccount);
        doNothing().when(mockAccountRepository).add(any(BankAccount.class));

        BankAccount created = accountService.createAccount(newName, balance);

        assertNotNull(created);
        assertEquals(newName, created.getName());
        assertEquals(balance, created.getBalance());
        assertNotNull(created.getId());

        verify(mockFactory, times(1)).createWithParams(any(BankAccountParams.class));
        verify(mockAccountRepository, times(1)).add(any(BankAccount.class));
    }

    @Test
    public void shouldFindAccountById() {
        UUID testId = UUID.randomUUID();
        BankAccount expectedAccount = new BankAccount(testId, "Существующий пользователь", new BigDecimal("5000"));

        when(mockAccountRepository.findById(testId)).thenReturn(expectedAccount);

        BankAccount found = accountService.findById(testId);

        assertNotNull(found);
        assertEquals(testId, found.getId());
        assertEquals("Существующий пользователь", found.getName());
        assertEquals(new BigDecimal("5000"), found.getBalance());

        verify(mockFactory, never()).createWithParams(any());
        verify(mockAccountRepository).findById(testId);
    }

    @Test
    public void shouldReturnNullWhenAccountNotFound() {
        UUID fakeId = UUID.randomUUID();

        when(mockAccountRepository.findById(fakeId)).thenReturn(null);

        BankAccount result = accountService.findById(fakeId);

        assertNull(result, "Должен вернуть null, если аккаунт не найден");

        verify(mockFactory, never()).createWithParams(any());
        verify(mockAccountRepository).findById(fakeId);
    }
}