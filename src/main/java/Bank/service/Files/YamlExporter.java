package Bank.service.Files;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;

import java.util.List;

public class YamlExporter implements Exporter {

    @Override
    public String getFileExtension() {
        return "yaml";
    }

    @Override
    public String exportAccounts(List<BankAccount> accounts) {
        if (accounts.isEmpty()) return "accounts: []\n";
        StringBuilder sb = new StringBuilder();
        sb.append("accounts:\n");
        for (BankAccount a : accounts) {
            sb.append("- id: \"").append(a.getId()).append("\"\n")
                    .append("  name: \"").append(escapeYaml(a.getName())).append("\"\n")
                    .append("  balance: ").append(a.getBalance()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String exportCategories(List<Category> categories) {
        if (categories.isEmpty()) return "categories: []\n";
        StringBuilder sb = new StringBuilder();
        sb.append("categories:\n");
        for (Category c : categories) {
            sb.append("- id: \"").append(c.getId()).append("\"\n")
                    .append("  name: \"").append(escapeYaml(c.getName())).append("\"\n")
                    .append("  direction: ").append(c.getFlowDirection().name()).append("\n"); // enum без кавычек (YAML позволяет)
        }
        return sb.toString();
    }

    @Override
    public String exportOperations(List<Operation> operations) {
        if (operations.isEmpty()) return "operations: []\n";
        StringBuilder sb = new StringBuilder();
        sb.append("operations:\n");
        for (Operation o : operations) {
            sb.append("- id: \"").append(o.getId()).append("\"\n")
                    .append("  account_id: \"").append(o.getBankAccountId()).append("\"\n")
                    .append("  category_id: \"").append(o.getCattegoryId()).append("\"\n")
                    .append("  direction: ").append(o.getFlowDirection().name()).append("\n")
                    .append("  amount: ").append(o.getAmount()).append("\n")
                    .append("  date: \"").append(o.getDate()).append("\"\n")
                    .append("  description: \"").append(escapeYaml(o.getDescription())).append("\"\n");
        }
        return sb.toString();
    }

    private String escapeYaml(String str) {
        if (str == null) return "";
        if (str.contains(":") || str.contains("{") || str.contains("}") ||
                str.contains("[") || str.contains("]") || str.contains(",")) {
            return "\"" + str.replace("\"", "\"\"") + "\"";
        }
        return str;
    }
}