package Bank.domain.Command;

import Bank.domain.enums.FlowDirection;
import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.model.Operation;
import Bank.service.AccountService;
import Bank.service.BalanceRecalculationService;
import Bank.service.CategoryService;
import Bank.service.OperationService;

import java.math.BigDecimal;
import java.util.UUID;

public class WithdrawCommand implements Command {

    private final AccountService accountService;
    private final OperationService operationService;
    private final CategoryService categoryService;
    private final BalanceRecalculationService recalculationService;

    private final String accountId;
    private final BigDecimal amount;
    private final String categoryName;

    private String operationId;

    public WithdrawCommand(AccountService accountService,
                           OperationService operationService,
                           CategoryService categoryService,
                           BalanceRecalculationService recalculationService,
                           String accountId,
                           BigDecimal amount,
                           String categoryName) {
        this.accountService = accountService;
        this.operationService = operationService;
        this.categoryService = categoryService;
        this.recalculationService = recalculationService;
        this.accountId = accountId;
        this.amount = amount;
        this.categoryName = categoryName;
    }

    @Override
    public void execute() {
        try {
            UUID uuid = UUID.fromString(accountId);
            BankAccount account = accountService.findById(uuid);

            if (account == null) {
                System.out.println("Счёт не найден: " + accountId);
                return;
            }

            Category category = categoryService.findByNameAndType(categoryName, FlowDirection.OUTCOME);
            if (category == null) {
                category = categoryService.createCategory(categoryName, FlowDirection.OUTCOME);
            }

            Operation operation = operationService.withdraw(account, amount, category, "Расход: " + categoryName);

            if (operation != null) {
                this.operationId = operation.getId().toString();
                System.out.println("Снято " + amount + " RUB");
            } else {
                System.out.println("Недостаточно средств");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный ID счёта: " + accountId);
        }
    }

    @Override
    public void undo() {
        if (operationId != null) {
            boolean success = operationService.deleteById(UUID.fromString(operationId));
            if (success) {
                recalculationService.recalculate(UUID.fromString(accountId));
                System.out.println("Операция отменена, баланс пересчитан");
            } else {
                System.out.println("Не удалось отменить операцию");
            }
        }
    }

    @Override
    public String getName() {
        return "Withdraw";
    }
}