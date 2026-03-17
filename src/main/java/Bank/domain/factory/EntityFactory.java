package Bank.domain.factory;

import Bank.domain.model.DomainEntity;
import Bank.domain.params.EntityParams;

import java.time.LocalDateTime;

public interface EntityFactory<T extends DomainEntity, P extends EntityParams> {
    T createWithParams(P params);
    private void entityCreated(T entity) {
        System.out.println("Создана сущность: " + entity.getClass().getSimpleName() + "[ID: " + entity.getId() + "]");
    } // Позже можно заменить на декоратор

}
