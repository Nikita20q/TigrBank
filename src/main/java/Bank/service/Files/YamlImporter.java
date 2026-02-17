package Bank.service.Files;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;
import Bank.domain.enums.FlowDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class YamlImporter implements Importer {

    @Override
    public List<BankAccount> importAccounts(String yamlData) {
        List<BankAccount> accounts = new ArrayList<>();
        List<Map<String, String>> items = parseYamlList(yamlData, "accounts");
        for (Map<String, String> item : items) {
            UUID id = UUID.fromString(item.get("id"));
            String name = item.get("name");
            BigDecimal balance = new BigDecimal(item.get("balance"));
            accounts.add(new BankAccount(id, name, balance));
        }
        return accounts;
    }

    @Override
    public List<Category> importCategories(String yamlData) {
        List<Category> categories = new ArrayList<>();
        List<Map<String, String>> items = parseYamlList(yamlData, "categories");
        for (Map<String, String> item : items) {
            UUID id = UUID.fromString(item.get("id"));
            String name = item.get("name");
            FlowDirection direction = FlowDirection.valueOf(item.get("direction"));
            categories.add(new Category(id, name, direction));
        }
        return categories;
    }

    @Override
    public List<Operation> importOperations(String yamlData) {
        List<Operation> operations = new ArrayList<>();
        List<Map<String, String>> items = parseYamlList(yamlData, "operations");
        for (Map<String, String> item : items) {
            UUID id = UUID.fromString(item.get("id"));
            UUID accountId = UUID.fromString(item.get("account_id"));
            UUID categoryId = UUID.fromString(item.get("category_id"));
            FlowDirection direction = FlowDirection.valueOf(item.get("direction"));
            BigDecimal amount = new BigDecimal(item.get("amount"));
            LocalDateTime date = LocalDateTime.parse(item.get("date"));
            String description = item.get("description");
            operations.add(new Operation(id, accountId, categoryId, direction, amount, date, description));
        }
        return operations;
    }

    // Упрощённый парсер YAML-списка
    private List<Map<String, String>> parseYamlList(String yaml, String rootKey) {
        List<Map<String, String>> result = new ArrayList<>();
        String[] lines = yaml.split("\n");
        Map<String, String> currentItem = null;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("- ")) {
                // Новый элемент списка
                if (currentItem != null) {
                    result.add(currentItem);
                }
                currentItem = new HashMap<>();
                // Обрабатываем первую строку элемента
                String remainder = line.substring(2);
                if (remainder.contains(":")) {
                    String[] kv = remainder.split(":", 2);
                    String key = kv[0].trim();
                    String value = kv[1].trim();
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    currentItem.put(key, value);
                }
            } else if (currentItem != null && line.contains(":")) {
                // Продолжение текущего элемента
                String[] kv = line.split(":", 2);
                String key = kv[0].trim();
                String value = kv[1].trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                currentItem.put(key, value);
            }
        }
        if (currentItem != null) {
            result.add(currentItem);
        }
        return result;
    }

    @Override
    public String getFileExtension() {
        return "yaml";
    }
}