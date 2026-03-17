package Bank.service.Files.factory;

import Bank.service.Files.Exporter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExporterFactory {

    private final Map<String, Exporter> exporters = new HashMap<>();

    public ExporterFactory(List<Exporter> exporterList) {
        for (Exporter exporter : exporterList) {
            String ext = exporter.getFileExtension().toLowerCase();
            exporters.put(ext, exporter);
            if ("yaml".equals(ext)) {
                exporters.put("yml", exporter);
            }
        }
    }

    public Exporter getExporter(String extension) {
        if (extension == null || extension.isEmpty()) {
            throw new IllegalArgumentException("Расширение файла не может быть пустым");
        }

        String ext = extension.toLowerCase().replace(".", "");
        Exporter exporter = exporters.get(ext);

        if (exporter == null) {
            throw new IllegalArgumentException("Неподдерживаемый формат экспорта: " + extension);
        }
        return exporter;
    }

    public boolean supports(String extension) {
        if (extension == null) return false;
        return exporters.containsKey(extension.toLowerCase().replace(".", ""));
    }
}