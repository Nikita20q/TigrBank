package Bank.service.Files;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;
import Bank.domain.enums.FlowDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class JsonExporter implements Exporter {

    @Override
    public String getFileExtension() {
        return "json";
    }

    @Override
    public String exportAccounts(List<BankAccount> accounts) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount a = accounts.get(i);
            sb.append("  {\n")
                    .append("    \"id\": \"").append(a.getId()).append("\",\n")
                    .append("    \"name\": \"").append(escapeJson(a.getName())).append("\",\n")
                    .append("    \"balance\": ").append(a.getBalance()).append("\n")
                    .append("  }");
            if (i < accounts.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String exportCategories(List<Category> categories) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < categories.size(); i++) {
            Category c = categories.get(i);
            sb.append("  {\n")
                    .append("    \"id\": \"").append(c.getId()).append("\",\n")
                    .append("    \"name\": \"").append(escapeJson(c.getName())).append("\",\n")
                    .append("    \"direction\": \"").append(c.getFlowDirection().name()).append("\"\n")
                    .append("  }");
            if (i < categories.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String exportOperations(List<Operation> operations) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < operations.size(); i++) {
            Operation o = operations.get(i);
            sb.append("  {\n")
                    .append("    \"id\": \"").append(o.getId()).append("\",\n")
                    .append("    \"account_id\": \"").append(o.getBankAccountId()).append("\",\n")
                    .append("    \"category_id\": \"").append(o.getCattegoryId()).append("\",\n")
                    .append("    \"type\": \"").append(o.getFlowDirection().name()).append("\",\n")
                    .append("    \"amount\": ").append(o.getAmount()).append(",\n")
                    .append("    \"date\": \"").append(o.getDate()).append("\",\n")
                    .append("    \"description\": \"").append(escapeJson(o.getDescription())).append("\"\n")
                    .append("  }");
            if (i < operations.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}