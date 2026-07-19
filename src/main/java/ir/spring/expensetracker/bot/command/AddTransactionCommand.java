package ir.spring.expensetracker.bot.command;

import ir.spring.expensetracker.bot.DisplayMessage;
import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.MaintainingDraft;
import ir.spring.expensetracker.bot.state.SessionManager;
import ir.spring.expensetracker.bot.state.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;;


@Component
public class AddTransactionCommand implements CommandHandler {

    private final SessionManager sessionManager;
    private final DisplayMessage displayMessage;

    public AddTransactionCommand(SessionManager sessionManager, DisplayMessage displayMessage) {
        this.sessionManager = sessionManager;
        this.displayMessage = displayMessage;
    }

    @Override
    public String getCommandName() {
        return "افزودن ➕";
    }

    @Override
    public void handleCommand(Update update) {
        displayMessage.displayMessage(update.getMessage().getChatId(), "مبلغی که خرج کردی یا گرفتی رو وارد کن");
        UserSession userSession = sessionManager.getUserSession(update.getMessage().getFrom().getId());
        sessionManager.updateState(update.getMessage().getFrom().getId(), ConversationState.AMOUNT);
        userSession.setMaintainingDraft(new MaintainingDraft());
    }
}
