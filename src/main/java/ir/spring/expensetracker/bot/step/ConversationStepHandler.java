package ir.spring.expensetracker.bot.step;

import ir.spring.expensetracker.bot.state.ConversationState;
import ir.spring.expensetracker.bot.state.UserSession;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConversationStepHandler {
    ConversationState getConversationState();
    void handle(Update update, UserSession userSession);
}
