package Bank.service.Files;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CsvImporter extends BaseImporter {

    @Override
    public String getFileExtension() {
        return "csv";
    }

    @Override
    protected List<Map<String, String>> parseRecords(String rawData) {
        List<Map<String, String>> records = new ArrayList<>();
        String[] lines = rawData.split("\n");

        if (lines.length < 2) {
            return records;
        }

        String[] headers = parseCsvLine(lines[0].trim());

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            String[] values = parseCsvLine(line);
            Map<String, String> record = new HashMap<>();

            for (int j = 0; j < Math.min(headers.length, values.length); j++) {
                record.put(headers[j], unescapeCsv(values[j]));
            }
            records.add(record);
        }
        return records;
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

    private String unescapeCsv(String value) {
        if (value == null) return "";
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() > 1) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value;
    }
}