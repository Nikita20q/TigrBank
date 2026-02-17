import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int input = -1;
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();
        do {
            System.out.println("Выберите тип операции:");
            System.out.println("""
                    1. Создать клиента
                    2. Открыть дебетовый счёт
                    3. Открыть кредитный счёт
                    4. Пополнить
                    5. Снять
                    6. Перевести
                    7. Показать счета клиента
                    8. Показать транзакции
                    9. Отчёт банка
                    10. Выход""");
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                scanner.nextLine();
            }
            switch (input) {
                case 1 :
                    System.out.println("Введите имя: ");
                    String name = scanner.nextLine();
                    bank.createCustomer(name);
                    break;
                case 2:
                    System.out.println("Введите customerId: ");
                    Customer debitAccount = bank.findCustomer(scanner.nextInt());
                    if (debitAccount == null) System.out.println("Клиент не найден");
                    else bank.openDebitAccount(debitAccount);
                    break;
                case 3:
                    System.out.println("Введите AccountNumber: ");
                    Customer creditAccount = bank.findCustomer(scanner.nextInt());
                    System.out.println("Введите лимит кредита: ");
                    double limit = scanner.nextDouble();
                    if (creditAccount == null) bank.openCreditAccount(creditAccount, limit);
                    else System.out.println("Клиент не найден");
                    break;
                case 4:
                    System.out.println("Введите AccountNumber: ");
                    Account depositAccount = bank.findAccount(scanner.nextLine());
                    System.out.println("Введите сумму пополнения: ");
                    int debit = scanner.nextInt();
                    if (depositAccount == null || !bank.deposit(depositAccount.getAccountNumber(), debit)) System.out.println("Ошибка пополнения баланса");
                    break;
                case 5:
                    System.out.println("Введите AccountNumber: ");
                    Account withAccount = bank.findAccount(scanner.nextLine());
                    System.out.println("Введите сумму вывода: ");
                    int with = scanner.nextInt();
                    if (withAccount != null || !bank.withdraw(withAccount.getAccountNumber(), with)) System.out.println("Ошибка снятия денег");
                    break;
                case 6:
                    System.out.println("Введите AccountNumber from: ");
                    Account from = bank.findAccount(scanner.nextLine());
                    System.out.println("Введите AccountNumber to: ");
                    Account to = bank.findAccount(scanner.nextLine());
                    System.out.println("Введите сумму перевода: ");
                    int transfer = scanner.nextInt();
                    if(from == null || to == null || !bank.transfer(from.getAccountNumber(), to.getAccountNumber(), transfer)) System.out.println("Ошибка перевода денег");
                    break;
                case 7:
                    System.out.println("Введите id клиента");
                    int id = scanner.nextInt();
                    bank.printCustomerAccounts(id);
                    break;
                case 8:
                    bank.printTransactions();
                    break;
                case 9:
                    bank.printReport();
                    break;
                default:
                    System.out.println("Некорректный ввода, попробуйте ещё раз");
            }
        } while (input != 10);
    }
}
