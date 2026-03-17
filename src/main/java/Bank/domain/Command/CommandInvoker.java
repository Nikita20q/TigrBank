package Bank.domain.Command;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
public class CommandInvoker {

    private final Deque<Command> history = new ArrayDeque<>();
    private final int maxHistorySize = 50;

    public void execute(Command command) {
        System.out.println("Выполнение: " + command.getName());
        command.execute();

        history.push(command);
        while (history.size() > maxHistorySize) {
            history.removeLast();
        }
    }

    public boolean undoLast() {
        if (history.isEmpty()) {
            System.out.println("История пуста");
            return false;
        }

        Command last = history.pop();
        System.out.println("Отмена: " + last.getName());
        last.undo();
        return true;
    }

    public void showHistory() {
        System.out.println("\n=== История команд ===");
        if (history.isEmpty()) {
            System.out.println("Пусто");
        } else {
            int i = 1;
            for (Command cmd : history) {
                System.out.println(i++ + ". " + cmd.getName());
            }
        }
        System.out.println();
    }
}