package ir.spring.expensetracker.bot.command;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.MainMenu;
import ir.spring.expensetracker.bot.state.SessionManager;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CancelCommand implements CommandHandler {

    private final SessionManager sessionManager;
    private final DisplayMessage displayMessage;

    public CancelCommand(SessionManager sessionManager, DisplayMessage displayMessage) {
        this.sessionManager = sessionManager;
        this.displayMessage = displayMessage;
    }

    @Override
    public String getCommandName() {
        return "بیخیال ❌";
    }

    @Override
    public void handleCommand(Update update) {
        sessionManager.clearSession(update.getMessage().getFrom().getId());
        sessionManager.clearSession(update.getMessage().getFrom().getId());
        displayMessage.displayKeyboardMessage(update.getMessage().getChatId(), "فرایند مورد نظر لغو شد. به منو اصلی برگشتیم", MainMenu.getMenu());
    }
}
