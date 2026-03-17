package Bank.service.Files;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JsonImporter extends BaseImporter {

    private static final Pattern FIELD_PATTERN = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"?([^\",}\\]]+)\"?");

    @Override
    public String getFileExtension() {
        return "json";
    }

    @Override
    protected List<Map<String, String>> parseRecords(String rawData) {
        List<Map<String, String>> records = new ArrayList<>();

        List<String> jsonObjects = extractJsonObjects(rawData);

        for (String jsonObject : jsonObjects) {
            Map<String, String> record = new HashMap<>();
            Matcher matcher = FIELD_PATTERN.matcher(jsonObject);

            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2).trim().replace("\"", "");
                record.put(key, value);
            }
            records.add(record);
        }
        return records;
    }

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
}