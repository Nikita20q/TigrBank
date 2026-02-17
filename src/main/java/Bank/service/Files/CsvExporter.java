package Bank.service.Files;

import Bank.domain.BankAccount;
import Bank.domain.Category;
import Bank.domain.Operation;

import java.util.List;

public class CsvExporter implements Exporter {

    @Override
    public String getFileExtension() {
        return "csv";
    }
    @Override
    public String exportAccounts(List<BankAccount> accounts) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,balance\n");
        for (BankAccount account : accounts) {
            sb.append(escape(account.getId().toString())).append(",")
                    .append(escape(account.getName())).append(",")
                    .append(escape(account.getBalance().toString()))
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public String exportCategories(List<Category> categories) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,name,direction\n");
        for (Category category : categories) {
            sb.append(escape(category.getId().toString())).append(",")
                    .append(escape(category.getName())).append(",")
                    .append(escape(category.getFlowDirection().name()))
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public String exportOperations(List<Operation>  operations) {
        StringBuilder sb = new StringBuilder();
        sb.append("id,account_id,category_id,type,amount,date,description\n");
        for (Operation operation : operations) {
            sb.append(escape(operation.getId().toString())).append(",")
                    .append(escape(operation.getBankAccountId().toString())).append(",")
                    .append(escape(operation.getCattegoryId().toString())).append(",")
                    .append(escape(operation.getFlowDirection().name())).append(",")
                    .append(escape(operation.getAmount().toString())).append(",")
                    .append(escape(operation.getDate().toString())).append(",")
                    .append(escape(operation.getDescription()))
                    .append("\n");
        }
        return sb.toString();
    }

    private String escape(Object value) {
        if (value == null) {
            return "";
        }
        String str = value.toString();
        if (str.isEmpty()) {
            return "";
        }
        if (str.contains(",") || str.contains("\"") || str.contains("\n") || str.contains("\r")) {
            str = str.replace("\"", "\"\"");
            return "\"" + str + "\"";
        }
        return str;
    }
}
