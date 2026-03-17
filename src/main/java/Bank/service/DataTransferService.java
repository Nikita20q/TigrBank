// Bank.service.DataTransferService
package Bank.service;

import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.model.Operation;
import Bank.repository.BankAccountRepository;
import Bank.repository.CategoryRepository;
import Bank.repository.OperationRepository;
import Bank.service.Files.Exporter;
import Bank.service.Files.Importer;
import Bank.service.Files.factory.ExporterFactory;
import Bank.service.Files.factory.ImporterFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class DataTransferService {

    private final ImporterFactory importerFactory;
    private final ExporterFactory exporterFactory;

    private final BankAccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final OperationRepository operationRepository;

    public DataTransferService(ImporterFactory importerFactory,
                               ExporterFactory exporterFactory,
                               BankAccountRepository accountRepository,
                               CategoryRepository categoryRepository,
                               OperationRepository operationRepository) {
        this.importerFactory = importerFactory;
        this.exporterFactory = exporterFactory;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public void importAll(String filename, String extension) throws IOException {
        Importer importer = importerFactory.getImporter(extension);
        String content = Files.readString(Path.of(filename));

        for (BankAccount account : importer.importAccounts(content)) {
            accountRepository.add(account);
        }
        for (Category category : importer.importCategories(content)) {
            categoryRepository.addCategory(category);
        }
        for (Operation operation : importer.importOperations(content)) {
            operationRepository.addOperation(operation);
        }

        System.out.println("Данные импортированы из: " + filename);
    }

    public void exportAll(String filename, String extension) throws IOException {
        Exporter exporter = exporterFactory.getExporter(extension);

        List<BankAccount> accounts = accountRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        List<Operation> operations = operationRepository.findAll();

        StringBuilder result = new StringBuilder();
        result.append(exporter.exportAccounts(accounts));
        result.append("\n");
        result.append(exporter.exportCategories(categories));
        result.append("\n");
        result.append(exporter.exportOperations(operations));

        Files.writeString(Path.of(filename), result.toString());
        System.out.println("Данные экспортированы в: " + filename);
    }

    public boolean canImport(String extension) {
        return importerFactory.supports(extension);
    }

    public boolean canExport(String extension) {
        return exporterFactory.supports(extension);
    }
}