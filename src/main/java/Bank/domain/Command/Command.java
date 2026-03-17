package Bank.domain.Command;

public interface Command {
    void execute();
    void undo();
    String getName();
}
