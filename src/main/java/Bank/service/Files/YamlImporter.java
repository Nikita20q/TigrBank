package Bank.service.Files;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

@Component
public class YamlImporter extends BaseImporter {

    @Override
    public String getFileExtension() {
        return "yaml";
    }

    @Override
    protected List<Map<String, String>> parseRecords(String rawData) {
        List<Map<String, String>> records = new ArrayList<>();

        Yaml yaml = new Yaml();
        Object loaded = yaml.load(rawData);

        if (loaded == null) {
            return records;
        }

        if (loaded instanceof List) {
            List<Map<String, Object>> yamlList = (List<Map<String, Object>>) loaded;

            for (Map<String, Object> yamlRecord : yamlList) {
                Map<String, String> record = new HashMap<>();
                for (Map.Entry<String, Object> entry : yamlRecord.entrySet()) {
                    record.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
                records.add(record);
            }
        }
        return records;
    }
}