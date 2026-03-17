package Bank.service.Files.factory;

import Bank.service.Files.Importer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ImporterFactory {

    private final Map<String, Importer> importers = new HashMap<>();

    public ImporterFactory(List<Importer> importerList) {
        for (Importer importer : importerList) {
            String ext = importer.getFileExtension().toLowerCase();
            importers.put(ext, importer);
            if ("yaml".equals(ext)) {
                importers.put("yml", importer);
            }
        }
    }

    public Importer getImporter(String extension) {
        if (extension == null || extension.isEmpty()) {
            throw new IllegalArgumentException("Расширение файла не может быть пустым");
        }

        String ext = extension.toLowerCase().replace(".", "");
        Importer importer = importers.get(ext);

        if (importer == null) {
            throw new IllegalArgumentException("Неподдерживаемый формат импорта: " + extension);
        }
        return importer;
    }

    public boolean supports(String extension) {
        if (extension == null) return false;
        return importers.containsKey(extension.toLowerCase().replace(".", ""));
    }
}