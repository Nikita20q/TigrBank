package bank.tests;

import Bank.domain.factory.CategoryFactory;
import Bank.domain.model.Category;
import Bank.domain.params.CategoryParams;
import Bank.domain.enums.FlowDirection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryFactoryTest {

    private CategoryFactory factory;

    @BeforeEach
    public void setup() {
        factory = new CategoryFactory();
    }

    @Test
    public void shouldCreateCategoryWithValidParams() {
        CategoryParams params = new CategoryParams(
                UUID.randomUUID(),
                "Кафе",
                FlowDirection.OUTCOME
        );

        Category result = factory.createWithParams(params);

        assertNotNull(result);
        assertEquals(params.id(), result.getId());
        assertEquals(params.categoryName(), result.getName());
        assertEquals(params.flowDirection(), result.getFlowDirection());
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
    public void shouldCreateCategoryWithIncomeDirection() {
        CategoryParams params = new CategoryParams(
                UUID.randomUUID(),
                "Зарплата",
                FlowDirection.INCOME
        );

        Category result = factory.createWithParams(params);

        assertEquals(FlowDirection.INCOME, result.getFlowDirection());
    }
}