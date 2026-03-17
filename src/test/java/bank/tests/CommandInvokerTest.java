package bank.tests;

import Bank.domain.Command.Command;
import Bank.domain.Command.CommandInvoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandInvokerTest {

    private CommandInvoker invoker;

    @BeforeEach
    public void setup() {
        invoker = new CommandInvoker();
    }

    @Test
    public void shouldExecuteCommandSuccessfully() {
        Command mockCommand = mock(Command.class);
        doNothing().when(mockCommand).execute();
        when(mockCommand.getName()).thenReturn("TestCommand");

        invoker.execute(mockCommand);

        verify(mockCommand, times(1)).execute();
    }

    @Test
    public void shouldUndoLastCommand() {
        Command mockCommand = mock(Command.class);
        doNothing().when(mockCommand).execute();
        doNothing().when(mockCommand).undo();
        when(mockCommand.getName()).thenReturn("TestCommand");

        invoker.execute(mockCommand);

        boolean result = invoker.undoLast();

        assertTrue(result);
        verify(mockCommand).undo();
    }

    @Test
    public void shouldReturnFalseWhenUndoWithEmptyHistory() {
        boolean result = invoker.undoLast();

        assertFalse(result);
    }

    @Test
    public void shouldShowHistoryWithoutErrors() {
        Command mockCommand = mock(Command.class);
        doNothing().when(mockCommand).execute();
        when(mockCommand.getName()).thenReturn("TestCommand");
        invoker.execute(mockCommand);

        assertDoesNotThrow(() -> invoker.showHistory());
    }
}