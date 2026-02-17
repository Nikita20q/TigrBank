package Bank.service.Files;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;

import java.util.List;

public interface Importer {
    List<BankAccount> importAccounts(String csvData);
    List<Category> importCategories(String csvData);
    List<Operation> importOperations(String csvData);
    String getFileExtension();
}