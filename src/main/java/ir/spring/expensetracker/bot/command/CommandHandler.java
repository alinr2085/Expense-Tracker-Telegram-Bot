package ir.spring.expensetracker.bot.command;


import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {
    String getCommandName();
    void handleCommand(Update update);
}
