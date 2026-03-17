package Bank.application;

import Bank.domain.Command.Command;
import Bank.domain.Command.CommandInvoker;
import Bank.domain.Command.DepositCommand;
import Bank.domain.Command.WithdrawCommand;
import Bank.domain.model.BankAccount;
import Bank.domain.model.Category;
import Bank.domain.enums.FlowDirection;
import Bank.domain.model.Operation;
import Bank.service.*;
import Bank.service.Files.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FinanceApplication {
    private final AnalyticsService analyticsService;
    private final BalanceRecalculationService balanceRecalculationService;
    private final OperationService operationService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final DataTransferService dataTransferService;
    private final CommandInvoker commandInvoker;
    public FinanceApplication(AnalyticsService analyticsService, BalanceRecalculationService balanceRecalculationService, OperationService operationService, AccountService accountService, CategoryService categoryService, DataTransferService dataTransferService, CommandInvoker commandInvoker) {
        this.analyticsService = analyticsService;
        this.balanceRecalculationService = balanceRecalculationService;
        this.operationService = operationService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.dataTransferService = dataTransferService;
        this.commandInvoker = commandInvoker;
    }

    public void createAccount(String name) {
        accountService.createAccount(name, BigDecimal.ZERO);
    }

    public void renameAccount(String id, String name) {
        BankAccount bankAccount = accountService.findById(UUID.fromString(id));
        bankAccount.setName(name);
        System.out.println("Имя аккаунта изменено на: " + name);
    }

    public void deposit(String accountIdStr, BigDecimal amount, String categoryName) {
        try {
            UUID accountId = UUID.fromString(accountIdStr);
            BankAccount account = accountService.findById(accountId);

            if (account == null) {
                System.out.println("Счёт не найден!");
                return;
            }
            Category category = getOrCreateCategory(categoryName, FlowDirection.INCOME);

            operationService.deposit(account, category, amount, "Пополнение: " + categoryName);

            System.out.println("Успешно пополнено на " + amount + " RUB (Категория: " + categoryName + ")");
            System.out.println("Новый баланс: " + account.getBalance());

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: Некорректный ID счёта.");
        }
    }

    public void withdraw(String accountIdStr, BigDecimal amount, String categoryName) {
        try {
            UUID accountId = UUID.fromString(accountIdStr);
            BankAccount account = accountService.findById(accountId);

            if (account == null) {
                System.out.println("Счёт не найден!");
                return;
            }

            Category category = getOrCreateCategory(categoryName, FlowDirection.OUTCOME);

            Operation operation = operationService.withdraw(account, amount, category, "Расход: " + categoryName);

            if (operation != null) {
                System.out.println("Успешно снято " + amount + " RUB (Категория: " + categoryName + ")");
                System.out.println("Новый баланс: " + account.getBalance());
            } else {
                System.out.println("Недостаточно средств на счёте");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: Некорректный ID.");
        }
    }

    public void listAllAccounts() {
        List<BankAccount> accounts = accountService.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("Список счетов пуст.");
            return;
        }

        System.out.println("\n=== Список всех счетов ===");
        System.out.printf("%-40s %-20s %-15s%n", "ID", "Имя", "Баланс");
        System.out.println("-".repeat(90));

        for (BankAccount account : accounts) {
            String fullId = account.getId().toString();

            System.out.printf("%-38s %-20s %-15s\n",
                    fullId,
                    account.getName(),
                    account.getBalance() + " RUB"
            );
        }
        System.out.println("-".repeat(90) + "\n");
    }

    public void deleteAccount(String idString) {
        try {
            UUID id = UUID.fromString(idString);
            boolean isDeleted = accountService.deleteById(id);

            if (isDeleted) {
                System.out.println("Счёт с ID " + id + " успешно удалён.");
            } else {
                System.out.println("Счёт с ID" + id + "не найден.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный формат ID. Введите полный UUID.");
        }
    }

    private Category getOrCreateCategory(String name, FlowDirection expectedType) {
        Category category = categoryService.findByNameAndType(name, expectedType);

        if (category == null) {
            System.out.println("Создание категории: " + name);
            category = categoryService.createCategory(name, expectedType);
        }
        return category;
    }

    public void showNetBalanceReport(String startDateStr, String endDateStr) {
        try {
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);

            if (start.isAfter(end)) {
                System.out.println("Ошибка: Дата начала не может быть позже даты окончания!");
                return;
            }

            LocalDateTime from = start.atStartOfDay();

            LocalDateTime to = end.atTime(23, 59, 59);

            BigDecimal netBalance = analyticsService.getNetIncome(from, to);

            System.out.println("\n=== ОТЧЁТ: БАЛАНС ЗА ПЕРИОД ===");
            System.out.println("Период: с " + start + " по " + end);
            System.out.println("---------------------------------");
            System.out.println("Результат: " + netBalance + " RUB");

            System.out.println("---------------------------------\n");

        } catch (DateTimeParseException e) {
            System.out.println("Ошибка формата даты! Используйте формат ГГГГ-ММ-ДД (например, 2026-02-01).");
        }
    }

    public void showCategoryReport(String startDateStr, String endDateStr) {
        try {
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);

            if (start.isAfter(end)) {
                System.out.println("Ошибка: Дата начала не может быть позже даты окончания!");
                return;
            }

            LocalDateTime from = start.atStartOfDay();
            LocalDateTime to = end.atTime(23, 59, 59);

            System.out.println("\n=== ОТЧЁТ ПО КАТЕГОРИЯМ ===");
            System.out.println("Период: с " + start + " по " + end);
            System.out.println();

            Map<Category, BigDecimal> incomes = analyticsService.getIncomesByCategory(from, to);
            System.out.println("ДОХОДЫ:");
            if (incomes.isEmpty()) {
                System.out.println("нет данных");
            } else {
                printCategoryMap(incomes);
            }

            System.out.println();

            Map<Category, BigDecimal> expenses = analyticsService.getExpensesByCategory(from, to);
            System.out.println("РАСХОДЫ:");
            if (expenses.isEmpty()) {
                System.out.println("нет данных");
            } else {
                printCategoryMap(expenses);
            }

            System.out.println("-----------------------------\n");

        } catch (DateTimeParseException e) {
            System.out.println("Ошибка формата даты! Используйте формат ГГГГ-ММ-ДД.");
        }
    }

    private void printCategoryMap(Map<Category, BigDecimal> data) {
        for (Map.Entry<Category, BigDecimal> entry : data.entrySet()) {
            Category cat = entry.getKey();
            BigDecimal sum = entry.getValue();
            System.out.printf("   %-20s: %10s RUB%n", cat.getName(), sum);
        }
    }
    public void importData(String filename, String extension) {
        try {
            if (!dataTransferService.canImport(extension)) {
                System.out.println("Формат не поддерживается: " + extension);
                return;
            }
            String fullPath = filename + "." + extension;
            dataTransferService.importAll(fullPath, extension);
            System.out.println("Импорт завершён");
        } catch (IOException e) {
            System.out.println("Ошибка импорта: " + e.getMessage());
        }
    }

    public void exportData(String filename, String extension) {
        try {
            if (!dataTransferService.canExport(extension)) {
                System.out.println("Формат не поддерживается: " + extension);
                return;
            }
            String fullPath = filename + "." + extension;
            dataTransferService.exportAll(fullPath, extension);
            System.out.println("Экспорт завершён");
        } catch (IOException e) {
            System.out.println("Ошибка экспорта: " + e.getMessage());
        }
    }

    public void recalculateBalance(String id) {
        UUID uuid = UUID.fromString(id);
        BankAccount bankAccount = accountService.findById(uuid);
        if (!balanceRecalculationService.checkBalance(bankAccount)) {
            System.out.println("Баланс счёта некорректен (расходы > доходы)");
        } else {
            System.out.println("Баланс счёта корректен");
        }
    }

    public void depositWithCommand(String accountId, BigDecimal amount, String categoryName) {
        Command cmd = new DepositCommand(
                accountService, operationService, categoryService,
                balanceRecalculationService, accountId, amount, categoryName
        );
        commandInvoker.execute(cmd);
    }

    public void withdrawWithCommand(String accountId, BigDecimal amount, String categoryName) {
        Command cmd = new WithdrawCommand(
                accountService, operationService, categoryService,
                balanceRecalculationService, accountId, amount, categoryName
        );
        commandInvoker.execute(cmd);
    }

    public void undoLast() {
        commandInvoker.undoLast();
    }

    public void showCommandHistory() {
        commandInvoker.showHistory();
    }
}