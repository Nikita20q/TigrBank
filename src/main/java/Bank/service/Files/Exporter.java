package Bank.service.Files;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;

import java.util.List;

public interface Exporter {
    String exportAccounts(List<BankAccount> accounts);
    String exportCategories(List<Category> categories);
    String exportOperations(List<Operation> operations);
    String getFileExtension();
}