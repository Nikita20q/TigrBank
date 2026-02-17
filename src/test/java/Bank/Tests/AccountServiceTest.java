package Bank.Tests;

import Bank.domain.BankAccount;
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
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        mockAccountRepository = Mockito.mock(BankAccountRepository.class);
        accountService = new AccountService(mockAccountRepository);
    }

    @Test
    public void createWithZeroBalance() {
        doNothing().when(mockAccountRepository).add(any(BankAccount.class));

        String newName = "Тестовый пользователь";
        BankAccount created = accountService.createAccount(newName, BigDecimal.ZERO);

        assertNotNull(created);
        assertEquals(newName, created.getName());
        assertEquals(BigDecimal.ZERO, created.getBalance());
        assertNotNull(created.getId());

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

        verify(mockAccountRepository).findById(testId);
    }

    @Test
    public void shouldReturnNullWhenAccountNotFound() {
        UUID fakeId = UUID.randomUUID();

        when(mockAccountRepository.findById(fakeId)).thenReturn(null);

        BankAccount result = accountService.findById(fakeId);

        assertNull(result, "Должен вернуть null, если аккаунт не найден");
        verify(mockAccountRepository).findById(fakeId);
    }
}