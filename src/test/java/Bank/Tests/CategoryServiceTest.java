package Bank.Tests;

import Bank.domain.Category;
import Bank.domain.enums.FlowDirection;
import Bank.repository.CategoryRepository;
import Bank.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    private CategoryRepository mockRepo;
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        mockRepo = Mockito.mock(CategoryRepository.class);
        categoryService = new CategoryService(mockRepo);
    }

    @Test
    public void shouldCreateCategorySuccessfully() {
        doNothing().when(mockRepo).addCategory(any(Category.class));

        String name = "Кафе";
        Category created = categoryService.createCategory(name, FlowDirection.OUTCOME);

        assertNotNull(created);
        assertEquals(name, created.getName());
        assertEquals(FlowDirection.OUTCOME, created.getDirection());
        assertNotNull(created.getId());

        verify(mockRepo, times(1)).addCategory(any(Category.class));
    }

    @Test
    public void shouldFindCategoryByNameAndType() {
        String name = "Зарплата";
        FlowDirection type = FlowDirection.INCOME;
        Category expected = new Category(UUID.randomUUID(), name, type);

        when(mockRepo.findByNameAndType(name, type)).thenReturn(expected);

        Category found = categoryService.findByNameAndType(name, type);

        assertNotNull(found);
        assertEquals(name, found.getName());
        assertEquals(type, found.getDirection());
        verify(mockRepo).findByNameAndType(name, type);
    }

    @Test
    public void shouldReturnNullIfCategoryNotFound() {
        when(mockRepo.findByNameAndType("Неизвестная", FlowDirection.OUTCOME)).thenReturn(null);

        Category found = categoryService.findByNameAndType("Неизвестная", FlowDirection.OUTCOME);

        assertNull(found);
        verify(mockRepo).findByNameAndType("Неизвестная", FlowDirection.OUTCOME);
    }

    @Test
    public void shouldDeleteExistingCategory() {
        UUID id = UUID.randomUUID();
        when(mockRepo.existsById(id)).thenReturn(true);
        doNothing().when(mockRepo).deleteById(id);

        categoryService.deleteById(id);

        verify(mockRepo).existsById(id);
        verify(mockRepo).deleteById(id);
    }

    @Test
    public void shouldNotDeleteIfCategoryNotFound() {
        UUID fakeId = UUID.randomUUID();
        when(mockRepo.existsById(fakeId)).thenReturn(false);

        categoryService.deleteById(fakeId);

        verify(mockRepo).existsById(fakeId);
        verify(mockRepo, never()).deleteById(fakeId);
    }
}