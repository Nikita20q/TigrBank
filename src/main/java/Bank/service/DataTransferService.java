package Bank.service;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;
import Bank.service.Files.Exporter;
import Bank.service.Files.Importer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@Service
public class DataTransferService {
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final OperationService operationService;

    public DataTransferService(AccountService accountService, CategoryService categoryService, OperationService operationService) {
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.operationService = operationService;
    }

    public void exportAll(String baseFilename, Exporter exporter) throws IOException {
        String ext = exporter.getFileExtension();

        String accountsData = exporter.exportAccounts(accountService.getAllAccounts());
        writeToFile(baseFilename + ".accounts." + ext, accountsData);

        String categoriesData = exporter.exportCategories(categoryService.getAllCategories());
        writeToFile(baseFilename + ".categories." + ext, categoriesData);

        String operationsData = exporter.exportOperations(operationService.getAllOperations());
        writeToFile(baseFilename + ".operations." + ext, operationsData);
    }

    public void importAll(String baseFilename, Importer importer) throws IOException {
        String ext = importer.getFileExtension();

        String accountsContent = readFromFile(baseFilename + ".accounts." + ext);
        List<BankAccount> accounts = importer.importAccounts(accountsContent);
        for (BankAccount account : accounts) {
            accountService.createAccount(account.getName(), account.getBalance());
        }

        String categoriesContent = readFromFile(baseFilename + ".categories." + ext);
        List<Category> categories = importer.importCategories(categoriesContent);
        for (Category category : categories) {
            categoryService.createCategory(category.getName(), category.getFlowDirection());
        }

        String operationsContent = readFromFile(baseFilename + ".operations." + ext);
        List<Operation> operations = importer.importOperations(operationsContent);
        for (Operation operation : operations) {
            BankAccount account = accountService.findById(operation.getBankAccountId());
            Category category = categoryService.findById(operation.getCattegoryId());

            if (operation.getFlowDirection().isIncome()) {
                operationService.deposit(account, category, operation.getAmount(), operation.getDescription());
            } else {
                operationService.withdraw(account, operation.getAmount(), category, operation.getDescription());
            }
        }
    }

    private void writeToFile(String filename, String content) throws IOException {
        Path path = Paths.get(filename);
        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    private String readFromFile(String filename) throws IOException {
        return Files.readString(Paths.get(filename), StandardCharsets.UTF_8);
    }
}