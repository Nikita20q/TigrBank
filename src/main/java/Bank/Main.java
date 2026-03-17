package Bank;

import Bank.application.FinanceApplication;
import Bank.service.*;
import Bank.service.Files.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Scanner;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner demo(FinanceApplication financeApplication) {
        return (args) -> {
            System.out.println("=".repeat(20) + " Запуск ТигрБанка " + "=".repeat(20));
            Scanner scanner = new Scanner(System.in);
            int choice = -1;
            while (choice != 20) {
                System.out.println("Введите желаемую операцию: ");
                System.out.println("1) Создать нового пользователя.");
                System.out.println("2) Удалить пользователя.");
                System.out.println("3) Переименовать пользователя.");
                System.out.println("4) Пополнить баланс пользователя.");
                System.out.println("5) Вывод со счёта: ");
                System.out.println("6) Аналитика доходов за период.");
                System.out.println("7) Аналитика расходов за период.");
                System.out.println("8) Экспорт в файл.");
                System.out.println("9) Импорт из файла.");
                System.out.println("10) Пересчёт баланса");
                System.out.println("11) Отменить последнюю операцию.");
                System.out.println("12) Показать историю команд.");
                System.out.println("0) Вывести всех пользователей.");
                System.out.println("20) Завершить программу");
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("Введите имя пользователя: ");
                        String name = scanner.nextLine();
                        financeApplication.createAccount(name);
                        break;
                    case 2:
                        System.out.println("Введите id пользователя: ");
                        financeApplication.deleteAccount(scanner.nextLine());
                        break;
                    case 3:
                        System.out.println("Введите id пользователя: ");
                        String id = scanner.nextLine();
                        System.out.println("Введите новое имя: ");
                        String newName = scanner.nextLine();
                        financeApplication.renameAccount(id, newName);
                        break;
                    case 4:
                        System.out.println("Введите id пользователя: ");
                        String idDeposit = scanner.nextLine();
                        System.out.println("Введите сумму пополнения: ");
                        BigDecimal depositAmount = new BigDecimal(scanner.nextLine().trim());
                        System.out.println("Введите название категории: ");
                        String depositCategory = scanner.nextLine();
                        financeApplication.depositWithCommand(idDeposit, depositAmount, depositCategory);
                        break;
                    case 5:
                        System.out.println("Введите id пользователя: ");
                        String idWithdraw = scanner.nextLine();
                        System.out.println("Введите сумму вывода: ");
                        BigDecimal withdrawAmount = new BigDecimal(scanner.nextLine().trim());
                        System.out.println("Введите название категории: ");
                        String withdrawCategory = scanner.nextLine();
                        financeApplication.withdrawWithCommand(idWithdraw, withdrawAmount, withdrawCategory);
                        break;
                    case 6:
                        System.out.print("Введите дату начала (ГГГГ-ММ-ДД): ");
                        String start1 = scanner.nextLine().trim();
                        System.out.print("Введите дату окончания (ГГГГ-ММ-ДД): ");
                        String end1 = scanner.nextLine().trim();
                        financeApplication.showNetBalanceReport(start1, end1);
                        break;
                    case 7:
                        System.out.print("Введите дату начала (ГГГГ-ММ-ДД): ");
                        String start2 = scanner.nextLine().trim();
                        System.out.print("Введите дату окончания (ГГГГ-ММ-ДД): ");
                        String end2 = scanner.nextLine().trim();
                        financeApplication.showCategoryReport(start2, end2);
                        break;
                    case 8:
                        System.out.print("Имя файла (без расширения): ");
                        String expFile = scanner.nextLine();

                        System.out.println("Выберите формат:");
                        System.out.println("1) CSV");
                        System.out.println("2) JSON");
                         System.out.println("3) YAML");
                        int exp = scanner.nextInt();
                        scanner.nextLine();

                        switch (exp) {
                            case 1:
                                financeApplication.exportData(expFile, "csv");
                                break;
                            case 2:
                                financeApplication.exportData(expFile, "json");
                                break;
                            case 3:
                                financeApplication.exportData(expFile, "yaml");
                                break;
                            default:
                                System.out.println("Неверный формат.");
                                break;
                        }
                        break;

                    case 9:
                        System.out.print("Имя файла (без расширения): ");
                        String impFile = scanner.nextLine();

                        System.out.println("Выберите формат источника: ");
                        System.out.println("1) CSV");
                        System.out.println("2) JSON");
                        System.out.println("3) YAML");
                        int imp = scanner.nextInt();
                        scanner.nextLine();

                        switch (imp) {
                            case 1:
                                financeApplication.importData(impFile, "csv");
                                break;
                            case 2:
                                financeApplication.importData(impFile, "json");
                                break;
                            case 3:
                                financeApplication.importData(impFile, "yaml");
                                break;
                            default:
                                System.out.println("Неверный формат.");
                                break;
                        }
                        break;

                    case 10:
                        System.out.println("Введите id пользователя: ");
                        String idCheck = scanner.nextLine();
                        financeApplication.recalculateBalance(idCheck);
                        break;
                    case 11:
                        financeApplication.undoLast();
                        break;
                    case 12:
                        financeApplication.showCommandHistory();
                        break;
                    case 0:
                        financeApplication.listAllAccounts();
                        break;
                    case 20:
                        break;
                    default:
                        System.out.println("Некорректный ввод, попробуйте ещё раз.");
                }
            }
        };
    }
}
