package Bank.service.Files;

import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.model.Operation;

import java.util.List;
import java.util.Map;

public abstract class BaseExporter implements Exporter {

    @Override
    public String exportAccounts(List<BankAccount> accounts) {
        StringBuilder result = new StringBuilder();
        result.append(buildHeader(getAccountHeaders()));
        for (BankAccount account : accounts) {
            try {
                Map<String, String> record = convertAccountToMap(account);
                result.append(formatRecord(record)).append("\n");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result.toString();
    }

    @Override
    public String exportCategories(List<Category> categories) {
        StringBuilder result = new StringBuilder();
        result.append(buildHeader(getCategoryHeaders()));
        for (Category category : categories) {
            try {
                Map<String, String> record = convertCategoryToMap(category);
                result.append(formatRecord(record)).append("\n");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result.toString();
    }
    @Override
    public String exportOperations(List<Operation> operations) {
        StringBuilder result = new StringBuilder();
        result.append(buildHeader(getOperationHeaders()));
        for (Operation operation : operations) {
            try {
                Map<String, String> record = convertOperationToMap(operation);
                result.append(formatRecord(record)).append("\n");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result.toString();
    }

    protected Map<String, String> convertAccountToMap(BankAccount account) {
        return Map.of(
                "id", account.getId().toString(),
                "name", account.getName(),
                "balance", account.getBalance().toString()
        );
    }
    protected Map<String, String> convertCategoryToMap(Category category) {
        return Map.of(
                "id", category.getId().toString(),
                "name", category.getName(),
                "direction", category.getFlowDirection().name()
        );
    }

    protected Map<String, String> convertOperationToMap(Operation operation) {
        return Map.of(
                "id", operation.getId().toString(),
                "account_id", operation.getBankAccountId().toString(),
                "category_id", operation.getCategoryId().toString(),
                "type", operation.getFlowDirection().name(),
                "amount", operation.getAmount().toString(),
                "date", operation.getDate().toString(),
                "description", operation.getDescription()
        );
    }


    protected String buildHeader(List<String> headers) {
        return String.join(",", headers) + "\n";
    }

    protected abstract List<String> getAccountHeaders();
    protected abstract List<String> getCategoryHeaders();
    protected abstract List<String> getOperationHeaders();

    protected abstract String formatRecord(Map<String, String> record);
}
