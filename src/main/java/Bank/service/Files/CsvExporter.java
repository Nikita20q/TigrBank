package Bank.service.Files;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CsvExporter extends BaseExporter {

    @Override
    public String getFileExtension() {
        return "csv";
    }

    @Override
    protected List<String> getAccountHeaders() {
        return List.of("id", "name", "balance");
    }

    @Override
    protected List<String> getCategoryHeaders() {
        return List.of("id", "name", "direction");
    }

    @Override
    protected List<String> getOperationHeaders() {
        return List.of("id", "account_id", "category_id", "type", "amount", "date", "description");
    }

    @Override
    protected String formatRecord(Map<String, String> record) {
        List<String> values = record.values().stream()
                .map(this::escapeCsv)
                .toList();
        return String.join(",", values);
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}