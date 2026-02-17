package Bank.service.Files;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;
import Bank.domain.enums.FlowDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonImporter implements Importer {

    // Регулярные выражения для извлечения полей из JSON-объекта
    private static final Pattern ID_PATTERN = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern NAME_PATTERN = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern BALANCE_PATTERN = Pattern.compile("\"balance\"\\s*:\\s*([\\d.]+)");
    private static final Pattern DIRECTION_PATTERN = Pattern.compile("\"direction\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern ACCOUNT_ID_PATTERN = Pattern.compile("\"account_id\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern CATEGORY_ID_PATTERN = Pattern.compile("\"category_id\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern TYPE_PATTERN = Pattern.compile("\"type\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("\"amount\"\\s*:\\s*([\\d.]+)");
    private static final Pattern DATE_PATTERN = Pattern.compile("\"date\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("\"description\"\\s*:\\s*\"([^\"]*)\"");

    @Override
    public List<BankAccount> importAccounts(String jsonData) {
        List<BankAccount> accounts = new ArrayList<>();
        List<String> objects = extractJsonObjects(jsonData);
        for (String obj : objects) {
            UUID id = extractUUID(obj, ID_PATTERN);
            String name = extractString(obj, NAME_PATTERN);
            BigDecimal balance = extractBigDecimal(obj, BALANCE_PATTERN);
            accounts.add(new BankAccount(id, name, balance));
        }
        return accounts;
    }

    @Override
    public List<Category> importCategories(String jsonData) {
        List<Category> categories = new ArrayList<>();
        List<String> objects = extractJsonObjects(jsonData);
        for (String obj : objects) {
            UUID id = extractUUID(obj, ID_PATTERN);
            String name = extractString(obj, NAME_PATTERN);
            FlowDirection direction = FlowDirection.valueOf(extractString(obj, DIRECTION_PATTERN));
            categories.add(new Category(id, name, direction));
        }
        return categories;
    }

    @Override
    public List<Operation> importOperations(String jsonData) {
        List<Operation> operations = new ArrayList<>();
        List<String> objects = extractJsonObjects(jsonData);
        for (String obj : objects) {
            UUID id = extractUUID(obj, ID_PATTERN);
            UUID accountId = extractUUID(obj, ACCOUNT_ID_PATTERN);
            UUID categoryId = extractUUID(obj, CATEGORY_ID_PATTERN);
            FlowDirection type = FlowDirection.valueOf(extractString(obj, TYPE_PATTERN));
            BigDecimal amount = extractBigDecimal(obj, AMOUNT_PATTERN);
            LocalDateTime date = LocalDateTime.parse(extractString(obj, DATE_PATTERN));
            String description = extractString(obj, DESCRIPTION_PATTERN);
            operations.add(new Operation(id, accountId, categoryId, type, amount, date, description));
        }
        return operations;
    }

    // Извлекает все объекты {...} из массива JSON
    private List<String> extractJsonObjects(String jsonArray) {
        List<String> objects = new ArrayList<>();
        int depth = 0;
        int start = -1;
        for (int i = 0; i < jsonArray.length(); i++) {
            char c = jsonArray.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start != -1) {
                    objects.add(jsonArray.substring(start, i + 1));
                }
            }
        }
        return objects;
    }

    private UUID extractUUID(String json, Pattern pattern) {
        Matcher m = pattern.matcher(json);
        if (m.find()) {
            return UUID.fromString(m.group(1));
        }
        throw new IllegalArgumentException("Missing required field in JSON");
    }

    private String extractString(String json, Pattern pattern) {
        Matcher m = pattern.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return ""; // описание может отсутствовать
    }

    private BigDecimal extractBigDecimal(String json, Pattern pattern) {
        Matcher m = pattern.matcher(json);
        if (m.find()) {
            return new BigDecimal(m.group(1));
        }
        throw new IllegalArgumentException("Missing required numeric field in JSON");
    }

    @Override
    public String getFileExtension() {
        return "json";
    }
}