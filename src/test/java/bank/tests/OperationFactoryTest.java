package bank.tests;

import Bank.domain.factory.OperationFactory;
import Bank.domain.model.Operation;
import Bank.domain.params.OperationParams;
import Bank.domain.enums.FlowDirection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OperationFactoryTest {

    private OperationFactory factory;

    @BeforeEach
    public void setup() {
        factory = new OperationFactory();
    }

    @Test
    public void shouldCreateOperationWithValidParams() {
        OperationParams params = new OperationParams(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                FlowDirection.INCOME,
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                "Тестовая операция"
        );

        Operation result = factory.createWithParams(params);

        assertNotNull(result);
        assertEquals(params.id(), result.getId());
        assertEquals(params.bankAccountId(), result.getBankAccountId());
        assertEquals(params.cattegoryId(), result.getCategoryId());
        assertEquals(params.flowDirection(), result.getFlowDirection());
        assertEquals(params.amount(), result.getAmount());
        assertEquals(params.description(), result.getDescription());
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
    public void shouldCreateOperationWithNullDescription() {
        OperationParams params = new OperationParams(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                FlowDirection.OUTCOME,
                new BigDecimal("50.00"),
                LocalDateTime.now(),
                null
        );

        Operation result = factory.createWithParams(params);

        assertNotNull(result);
        assertNull(result.getDescription());
    }
}