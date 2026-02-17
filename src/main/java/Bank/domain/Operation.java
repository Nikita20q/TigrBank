package Bank.domain;

import Bank.domain.enums.FlowDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Operation {
    private UUID id;
    private FlowDirection flowDirection;
    private UUID bankAccountId;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private UUID cattegoryId;
    public Operation(UUID id, UUID bankAccountId, UUID cattegoryId ,FlowDirection flowDirection, BigDecimal amount, LocalDateTime date, String description) {
        this.id = id;
        this.flowDirection = flowDirection;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.cattegoryId = cattegoryId;
        this.bankAccountId = bankAccountId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public FlowDirection getFlowDirection() {
        return flowDirection;
    }

    public UUID getCattegoryId() {
        return cattegoryId;
    }

    public UUID getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
}
