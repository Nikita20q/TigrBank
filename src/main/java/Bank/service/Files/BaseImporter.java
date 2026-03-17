package Bank.service.Files;

import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.model.Operation;
import Bank.domain.enums.FlowDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public abstract class BaseImporter implements Importer {

    @Override
    public final List<BankAccount> importAccounts(String rawData) {
        List<Map<String, String>> records = parseRecords(rawData);
        List<BankAccount> accounts = new ArrayList<>();

        for (Map<String, String> record : records) {
            try {
                if (validateAccountRecord(record)) {
                    BankAccount account = createAccount(record);
                    accounts.add(account);
                }
            } catch (Exception e) {
                handleImportError("Account", record, e);
            }
        }
        return accounts;
    }

    @Override
    public final List<Category> importCategories(String rawData) {
        List<Map<String, String>> records = parseRecords(rawData);
        List<Category> categories = new ArrayList<>();

        for (Map<String, String> record : records) {
            try {
                if (validateCategoryRecord(record)) {
                    Category category = createCategory(record);
                    categories.add(category);
                }
            } catch (Exception e) {
                handleImportError("Category", record, e);
            }
        }
        return categories;
    }

    @Override
    public final List<Operation> importOperations(String rawData) {
        List<Map<String, String>> records = parseRecords(rawData);
        List<Operation> operations = new ArrayList<>();

        for (Map<String, String> record : records) {
            try {
                if (validateOperationRecord(record)) {
                    Operation operation = createOperation(record);
                    operations.add(operation);
                }
            } catch (Exception e) {
                handleImportError("Operation", record, e);
            }
        }
        return operations;
    }

    protected boolean validateAccountRecord(Map<String, String> record) {
        return record.containsKey("id") &&
                record.containsKey("name") &&
                record.containsKey("balance") &&
                record.get("id") != null &&
                !record.get("id").isBlank();
    }

    protected boolean validateCategoryRecord(Map<String, String> record) {
        return record.containsKey("id") &&
                record.containsKey("name") &&
                record.containsKey("direction");
    }

    protected boolean validateOperationRecord(Map<String, String> record) {
        return record.containsKey("id") &&
                record.containsKey("account_id") &&
                record.containsKey("category_id") &&
                record.containsKey("type") &&
                record.containsKey("amount");
    }

    protected BankAccount createAccount(Map<String, String> record) {
        UUID id = parseUUID(record.get("id"));
        String name = record.get("name");
        BigDecimal balance = parseBigDecimal(record.get("balance"));
        return new BankAccount(id, name, balance);
    }

    protected Category createCategory(Map<String, String> record) {
        UUID id = parseUUID(record.get("id"));
        String name = record.get("name");
        String direction = record.get("direction").toUpperCase();
        FlowDirection flowDirection = FlowDirection.valueOf(direction);
        return new Category(id, name, flowDirection);
    }

    protected Operation createOperation(Map<String, String> record) {
        UUID id = parseUUID(record.get("id"));
        UUID accountId = parseUUID(record.get("account_id"));
        UUID categoryId = parseUUID(record.get("category_id"));
        String type = record.get("type").toUpperCase();
        FlowDirection flowDirection = FlowDirection.valueOf(type);
        BigDecimal amount = parseBigDecimal(record.get("amount"));
        LocalDateTime date = parseLocalDateTime(record.get("date"));
        String description = record.getOrDefault("description", "");

        return new Operation(id, accountId, categoryId, flowDirection, amount, date, description);
    }

    protected void handleImportError(String entityType, Map<String, String> record, Exception e) {
        System.err.println("Ошибка импорта " + entityType + ": " + e.getMessage());
        System.err.println("Запись: " + record);
    }

    protected UUID parseUUID(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UUID не может быть пустым");
        }
        return UUID.fromString(value.trim());
    }

    protected BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value.trim());
    }

    protected LocalDateTime parseLocalDateTime(String value) {
        if (value == null || value.isBlank()) {
            return LocalDateTime.now();
        }
        return LocalDateTime.parse(value.trim());
    }
    protected abstract List<Map<String, String>> parseRecords(String rawData);
}