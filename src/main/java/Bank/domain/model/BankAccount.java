package Bank.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class BankAccount implements DomainEntity {
    private final UUID id;
    private String name;
    private BigDecimal balance;
    public BankAccount(UUID id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        System.out.println("Создан новый аккаунт.");
        System.out.println("Имя: " + name);
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
    public boolean withdraw(BigDecimal amount) {
        if (balance.subtract(amount).compareTo(BigDecimal.ZERO) >= 0) {
            this.balance = this.balance.subtract(amount);
            return true;
        }
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", balance: " + balance;
    }

    public String getName() {
        return name;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
