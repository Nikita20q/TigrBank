package Bank.domain;

import Bank.domain.enums.FlowDirection;

import java.util.UUID;

public class Category {
    private UUID id;
    private String categoryName;
    private FlowDirection flowDirection;
    public Category(UUID id, String categoryName, FlowDirection flowDirection) {
        this.id = id;
        this.categoryName = categoryName;
        this.flowDirection = flowDirection;
    }
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
