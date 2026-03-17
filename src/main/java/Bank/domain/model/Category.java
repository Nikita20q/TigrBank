package Bank.domain.model;

import Bank.domain.enums.FlowDirection;

import java.util.UUID;

public class Category implements DomainEntity {
    private UUID id;
    private String categoryName;
    private FlowDirection flowDirection;
    public Category(UUID id, String categoryName, FlowDirection flowDirection) {
        this.id = id;
        this.categoryName = categoryName;
        this.flowDirection = flowDirection;
    }

    @Override
    public UUID getId() {
        return id;
    }
    public FlowDirection getFlowDirection() {
        return flowDirection;
    }

    public String getName() {
        return categoryName;
    }
}
