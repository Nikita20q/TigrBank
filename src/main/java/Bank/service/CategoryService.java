package Bank.service;

import Bank.domain.Category;
import Bank.domain.enums.FlowDirection;
import Bank.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, FlowDirection type) {
        Category category = new Category(UUID.randomUUID(), name, type);
        categoryRepository.addCategory(category);
        return category;
    }

    public Category findById(UUID id) {
        return categoryRepository.findById(id);
    }

    public Category findByNameAndType(String name, FlowDirection type) {
        return categoryRepository.findByNameAndType(name, type);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteById(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
    }
}