package Bank.domain.enums;

public enum FlowDirection {
    INCOME,
    OUTCOME;

    public boolean isIncome() {
        return this == INCOME;
    }

    public boolean isOutcome() {
        return this == OUTCOME;
    }
}
