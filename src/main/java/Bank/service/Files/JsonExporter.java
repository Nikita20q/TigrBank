package Bank.service.Files;

import Bank.domain.model.BankAccount;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JsonExporter extends BaseExporter {

    @Override
    public String getFileExtension() {
        return "json";
    }

    @Override
    protected List<String> getAccountHeaders() { return List.of(); }
    @Override
    protected List<String> getCategoryHeaders() { return List.of(); }
    @Override
    protected List<String> getOperationHeaders() { return List.of(); }

    @Override
    protected String buildHeader(List<String> headers) {
        return "[\n";
    }

    @Override
    protected String formatRecord(Map<String, String> record) {
        StringBuilder sb = new StringBuilder("  {");
        boolean first = true;
        for (Map.Entry<String, String> entry : record.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\n    \"")
                    .append(entry.getKey())
                    .append("\": \"")
                    .append(escapeJson(entry.getValue()))
                    .append("\"");
            first = false;
        }
        sb.append("\n  }");
        return sb.toString();
    }
    @Override
    public final String exportAccounts(List<BankAccount> accounts) {
        StringBuilder result = new StringBuilder("[\n");
        for (int i = 0; i < accounts.size(); i++) {
            try {
                Map<String, String> record = convertAccountToMap(accounts.get(i));
                result.append(formatRecord(record));
                if (i < accounts.size() - 1) result.append(",");
                result.append("\n");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        result.append("]");
        return result.toString();
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}