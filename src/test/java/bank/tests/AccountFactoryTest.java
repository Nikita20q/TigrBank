package bank.tests;

import Bank.domain.factory.AccountFactory;
import Bank.domain.model.BankAccount;
import Bank.domain.params.BankAccountParams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountFactoryTest {

    private AccountFactory factory;

    @BeforeEach
    public void setup() {
        factory = new AccountFactory();
    }

    @Test
    public void shouldCreateAccountWithValidParams() {
        BankAccountParams params = new BankAccountParams(
                UUID.randomUUID(),
                "Тестовый счёт",
                new BigDecimal("1000.00")
        );

        BankAccount result = factory.createWithParams(params);

        assertNotNull(result);
        assertEquals(params.id(), result.getId());
        assertEquals(params.name(), result.getName());
        assertEquals(params.balance(), result.getBalance());
    }

    @Test
    public void shouldThrowExceptionWhenParamsIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> factory.createWithParams(null)
        );

        assertEquals("params is null", exception.getMessage());
    }

    @Test
    public void shouldCreateAccountWithZeroBalance() {
        BankAccountParams params = new BankAccountParams(
                UUID.randomUUID(),
                "Новый счёт",
                BigDecimal.ZERO
        );

        BankAccount result = factory.createWithParams(params);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getBalance());
    }
}