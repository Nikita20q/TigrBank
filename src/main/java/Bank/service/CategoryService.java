package Bank.service;

import Bank.domain.factory.CategoryFactory;
import Bank.domain.model.Category;
import Bank.domain.enums.FlowDirection;
import Bank.domain.params.CategoryParams;
import Bank.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryFactory categoryFactory;

    public CategoryService(CategoryRepository categoryRepository, CategoryFactory categoryFactory) {
        this.categoryRepository = categoryRepository;
        this.categoryFactory = categoryFactory;
    }

    public Category createCategory(String name, FlowDirection type) {
        CategoryParams categoryParams = new CategoryParams(UUID.randomUUID(), name, type);

        Category category = categoryFactory.createWithParams(categoryParams);
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