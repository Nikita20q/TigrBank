package Bank.domain.model;

import Bank.domain.enums.FlowDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Operation implements DomainEntity {
    private UUID id;
    private FlowDirection flowDirection;
    private UUID bankAccountId;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private UUID categoryId;
    public Operation(UUID id, UUID bankAccountId, UUID cattegoryId ,FlowDirection flowDirection, BigDecimal amount, LocalDateTime date, String description) {
        this.id = id;
        this.flowDirection = flowDirection;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = cattegoryId;
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

    public UUID getCategoryId() {
        return categoryId;
    }

    @Override
    public UUID getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }

    public void undo(BankAccount account) {
        if (account == null) {
            throw new IllegalArgumentException("Счёт не может быть null");
        }
        if (!account.getId().equals(this.bankAccountId)) {
            throw new IllegalArgumentException("Операция не принадлежит этому счёту");
        }

        if (this.flowDirection == FlowDirection.INCOME) {
            account.withdraw(this.amount);
        } else {
            account.deposit(this.amount);
        }
    }
}
