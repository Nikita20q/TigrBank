package Bank.service.Files;

import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.model.Operation;

import java.util.List;

public interface Importer {
    List<BankAccount> importAccounts(String csvData);
    List<Category> importCategories(String csvData);
    List<Operation> importOperations(String csvData);
    String getFileExtension();
}