package ir.spring.expensetracker.bot.command;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CommandManager {

    private final List<CommandHandler> commands;

    public CommandManager(List<CommandHandler> commands) {
        this.commands = commands;
    }

    public Optional<CommandHandler> findCommand(String commandName) {
        return commands.stream().filter(c -> c.getCommandName().equals(commandName)).findFirst();
    }

}
