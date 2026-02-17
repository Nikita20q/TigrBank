package Bank.service.Files;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;
import Bank.domain.enums.FlowDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class CsvImporter implements Importer {

    @Override
    public List<BankAccount> importAccounts(String csvData) {
        List<BankAccount> accounts = new ArrayList<>();
        String[] lines = csvData.split("\n");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String[] parts = parseCsvLine(line);
            UUID id = UUID.fromString(parts[0]);
            String name = unescape(parts[1]);
            BigDecimal balance = new BigDecimal(unescape(parts[2]));
            accounts.add(new BankAccount(id, name, balance));
        }
        return accounts;
    }

    @Override
    public List<Category> importCategories(String csvData) {
        List<Category> categories = new ArrayList<>();
        String[] lines = csvData.split("\n");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String[] parts = parseCsvLine(line);
            UUID id = UUID.fromString(parts[0]);
            String name = unescape(parts[1]);
            FlowDirection direction = FlowDirection.valueOf(unescape(parts[2]));
            categories.add(new Category(id, name, direction));
        }
        return categories;
    }

    @Override
    public List<Operation> importOperations(String csvData) {
        List<Operation> operations = new ArrayList<>();
        String[] lines = csvData.split("\n");
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String[] parts = parseCsvLine(line);
            UUID id = UUID.fromString(parts[0]);
            UUID accountId = UUID.fromString(parts[1]);
            UUID categoryId = UUID.fromString(parts[2]);
            FlowDirection type = FlowDirection.valueOf(unescape(parts[3]));
            BigDecimal amount = new BigDecimal(unescape(parts[4]));
            LocalDateTime date = LocalDateTime.parse(unescape(parts[5]));
            String description = unescape(parts[6]);
            operations.add(new Operation(id, accountId, categoryId, type, amount, date, description));
        }
        return operations;
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    private String unescape(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value;
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }
}