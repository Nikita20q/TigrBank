package Bank.repository;

import Bank.domain.Category;
import Bank.domain.enums.FlowDirection;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CategoryRepository {
    private final List<Category> categories = new ArrayList<>();

    public void addCategory(Category category) {
        categories.add(category);
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public Category findById(UUID id) {
        for (Category category : categories) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }
    public Category findByNameAndType(String name, FlowDirection type) {
        for (Category category : categories) {
            if (category.getName().equals(name) && category.getFlowDirection() == type) {
                return category;
            }
        }
        return null;
    }

    public void deleteById(UUID id) {
        categories.removeIf(c -> c.getId().equals(id));
    }

    public boolean existsById(UUID id) {
        return findById(id) != null;
    }

    public List<Category> findAll() {
        return getCategories();
    }
}