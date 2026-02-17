import java.time.LocalDateTime;

public class Transaction {
    private types type;
    private double amount;
    private int fromAccountNumber;
    private int toAccountNumber;
    private LocalDateTime timestamp;
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public double getAmount() {
        return amount;
    }

    public int getFromAccountNumber() {
        return fromAccountNumber;
    }

    public int getToAccountNumber() {
        return toAccountNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public types getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Тип транзакции: " + type + "сумма: " + amount + "успешно: " + success;
    }
}
