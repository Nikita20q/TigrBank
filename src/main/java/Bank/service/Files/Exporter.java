package Bank.service.Files;

import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.model.Operation;

import java.util.List;

public interface Exporter {
    String exportAccounts(List<BankAccount> accounts);
    String exportCategories(List<Category> categories);
    String exportOperations(List<Operation> operations);
    String getFileExtension();
}