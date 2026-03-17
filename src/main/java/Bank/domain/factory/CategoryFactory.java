package Bank.domain.factory;

import Bank.domain.model.Category;
import Bank.domain.params.CategoryParams;
import org.springframework.stereotype.Service;

@Service
public class CategoryFactory implements EntityFactory <Category, CategoryParams> {

    @Override
    public Category createWithParams(CategoryParams params) {
        if (params == null) {
            throw new IllegalArgumentException("params is null");
        }
        return new Category(params.id(), params.categoryName(), params.flowDirection());
    }
}
