package Bank.service.Files;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class YamlExporter extends BaseExporter {

    @Override
    public String getFileExtension() {
        return "yaml";
    }

    @Override
    protected List<String> getAccountHeaders() { return List.of(); }
    @Override
    protected List<String> getCategoryHeaders() { return List.of(); }
    @Override
    protected List<String> getOperationHeaders() { return List.of(); }

    @Override
    protected String formatRecord(Map<String, String> record) {
        StringBuilder sb = new StringBuilder("- ");
        String fields = record.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining(", "));
        return sb.append(fields).toString();
    }

    @Override
    protected String buildHeader(List<String> headers) {
        return "";
    }
}